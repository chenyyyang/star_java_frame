package com.star.frame.component.api.json;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.cat.Cat;
import com.google.common.util.concurrent.RateLimiter;
import com.star.frame.component.api.annotation.OpenAPI;
import com.star.frame.component.api.annotation.OpenAPIMethod;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.star.frame.component.api.IAPISign;
import com.star.frame.component.api.IAPIVerify;
import com.star.frame.component.api.MobileInfo;
import com.star.frame.component.api.OpenAPINoneVerify;
import com.star.frame.core.CoreConstants;
import com.star.frame.core.support.SpringContextLoader;
import com.star.frame.core.support.exception.ServiceException;
import com.star.frame.core.support.exception.ServiceWarn;
import com.star.frame.core.support.local.LocalAttributeHolder;
import com.star.frame.core.support.pageLimit.PageLimitHolderFilter;
import com.star.frame.core.util.ClassUtilsEx;
import com.star.frame.core.util.CommonUtilsEx;
import com.star.frame.core.util.JSONUtilsEx;
import com.star.frame.core.util.ServletUtilsEx;
import com.star.frame.core.util.SignatureUtils;
import com.star.frame.core.util.StringUtilsEx;
import com.star.frame.core.util.SystemUtilsEx;

/**
 * 手机服务器端请求
 *
 * @author TYOTANN
 */
public class OpenAPIJsonServlet extends HttpServlet {

    private static final long serialVersionUID = -7813617057723596671L;

    private static Logger logger = LoggerFactory.getLogger(OpenAPIJsonServlet.class);

    private Map<String, Method> mobileMethodMap = new HashMap<String, Method>();

    private Map<String, OpenAPIMethod> mobileMethodAnnoMap = new HashMap<String, OpenAPIMethod>();

    private Map<String, IAPISign> signclassMap = new HashMap<String, IAPISign>();

    private String servletName;

    private String ipRanges;

    private double rateLimit;

    private RateLimiter rateLimiter;



    /**
     * 初始化，扫描系统中所有的mobileMethod
     */
    @Override
    public void init() throws ServletException {

        try {
            Enumeration<String> paramEnum = getInitParameterNames();

            if (paramEnum != null) {
                while (paramEnum.hasMoreElements()) {
                    String paramName = paramEnum.nextElement();

                    if ("signclass".equals(paramName)) {
                        signclassMap.put("default", (IAPISign) ClassUtils.getClass(getInitParameter(paramName)).newInstance());
                    } else if (StringUtils.startsWith(paramName, "signclass-")) {
                        signclassMap.put(StringUtils.substring(paramName, "signclass-".length()),
                                (IAPISign) ClassUtils.getClass(getInitParameter(paramName)).newInstance());
                    }
                }
            }

            this.servletName = StringUtils.defaultIfEmpty(getInitParameter("servletName"), StringUtils.EMPTY);
            this.ipRanges = getInitParameter("ipRanges");
            this.rateLimit = Double.parseDouble(StringUtils.defaultIfEmpty(getInitParameter("rateLimit"), "0"));
            if(this.rateLimit > 0.0) {
                this.rateLimiter = RateLimiter.create(rateLimit);
            }

            Map<String, Object> openClz = SpringContextLoader.getBeansWithAnnotation(OpenAPI.class);

            if (openClz != null) {
                for (Object clzObj : openClz.values()) {

                    List<Method> methodList = ClassUtilsEx.getClassMethodByAnnotation(clzObj.getClass(), OpenAPIMethod.class);

                    for (Method method : methodList) {

                        OpenAPIMethod methodAnno = method.getAnnotation(OpenAPIMethod.class);

                        String methodName = methodAnno.methodName();

                        if (StringUtils.isBlank(methodName)) {
                            methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
                        }

                        // 接口支持版本号
                        if (StringUtils.isNotBlank(methodAnno.version())) {
                            methodName = methodName + "#" + methodAnno.version();
                        }

                        if (servletName.equals(methodAnno.servletName())) {

                            if (mobileMethodMap.containsKey(methodName) || mobileMethodAnnoMap.containsKey(methodName)) {
                                throw new ServiceException("API接口:" + methodName + "有重复定义,请检查代码!");
                            }

                            mobileMethodMap.put(methodName, method);
                            mobileMethodAnnoMap.put(methodName, methodAnno);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MJSONResultEntity result = new MJSONResultEntity();

        // 得到参数
        Map<String, Object> paramMap = null;

        try {

            // 限流
            if(rateLimit > 0.0 && !rateLimiter.tryAcquire()) {
                throw new ServiceException(MJSONResultEntity.BIZ_BUSY,"访问频率超过限制");
            }

            // 访问IP检查
            if (StringUtils.isNotBlank(ipRanges) && !CommonUtilsEx.isIPAddressInRange(SystemUtilsEx.getClientIpSingle(request), ipRanges)) {
                throw new ServiceException("IP地址不允许访问");
            }

            // 得到参数
            paramMap = getParam(request, response);

            if (StringUtils.isBlank(request.getPathInfo())) {
                throw new ServiceException("请求的接口格式错误");
            }

            // 函数名
            String methodName = request.getPathInfo().substring(1);

            // 这里塞的时候不带版本号
            paramMap.put("FRAMEmethodName", methodName);

            // 调用函数版本号:如果不在header中,则从param中获得
            String methodVersion = request.getHeader("X-SC-version");
            methodVersion = StringUtils.isNotBlank(methodVersion) ? methodVersion : (String) paramMap.get("X-SC-version");

            // 函数名带版本号信息,用于反射
            String methodNameWithVersion = StringUtils.isBlank(methodVersion) ? methodName : methodName + "#" + methodVersion;

            OpenAPIMethod mobileMethod = mobileMethodAnnoMap.get(methodNameWithVersion);

            // 基本检查
            {
                if (mobileMethod == null) {
                    throw new ServiceException("请求的接口:" + methodNameWithVersion + "在服务器端没有定义,请检查!");
                }
            }

            // 校验检查
            if (mobileMethod.verify() != OpenAPINoneVerify.class) {

                IAPIVerify verifyMethod = SpringContextLoader.getBean(mobileMethod.verify());

                if (verifyMethod != null) {
                    boolean isVerify = verifyMethod.verify(paramMap);

                    if (!isVerify) {
                        throw new ServiceException(MJSONResultEntity.RESULT_SESSION_ERROR, "登录失败,请登录!");
                    }
                } else {
                    throw new ServiceException("未找到定义的类:" + mobileMethod.verify());
                }
            }

            // 请求方法
            result.setData(requestMethod(methodNameWithVersion, mobileMethod, paramMap));

        } catch (Exception e) {

            Throwable rootThrowable = ExceptionUtils.getRootCause(e) == null ? e : ExceptionUtils.getRootCause(e);

            String errorText = rootThrowable.getMessage() == null ? String.valueOf(rootThrowable) : rootThrowable.getMessage();

            if (rootThrowable instanceof ServiceException) {

                ServiceException se = ((ServiceException) rootThrowable);

                // 如果存在code,但是无text,且定义了message_i18n,则从message中取得具体的text值
                errorText = se.getMessage();

                logger.debug(errorText);

                if (StringUtils.isNotBlank(se.getCode())) {
                    result.setCode(se.getCode());
                } else {
                    result.setCode(MJSONResultEntity.RESULT_LOGIC_ERROR);
                }

                if (se.getData() != null) {
                    result.setData(se.getData());
                }
            } else if (rootThrowable instanceof ServiceWarn) {
                logger.debug(rootThrowable.getMessage());
                result.setCode(MJSONResultEntity.RESULT_WARN);
            } else {
                Cat.logError(rootThrowable);
                logger.error(rootThrowable.getMessage(), rootThrowable);
                result.setCode(MJSONResultEntity.RESULT_EXCEPTION);
            }

            result.setText(errorText);
        } finally {
            // 如果是导出文件,则默认认为OutPutStream已经关闭,这里不再输出
            if(!isExportFile(response)) {
                ServletUtilsEx.renderJson(response, result);
            }
        }
    }

    private boolean isExportFile(HttpServletResponse response) {
        return response.getContentType() != null && (response.getContentType().contains("excel")
                || response.getContentType().contains("pdf")
                || response.getContentType().contains("xls"));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void destroy() {
        mobileMethodMap.clear();
        mobileMethodAnnoMap.clear();
    }

    /**
     * 得到request中的参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getParam(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // web中接口请求，用jquery的ajax post参数后发现如果参数中有多个且有数组，则参数名是xxx[1]这样的格式
        String paramJsonStr = request.getParameter("FRAMEparams");

        // 得到参数
        Map<String, Object> paramMap = null;

        if (StringUtils.isNotBlank(paramJsonStr)) {
            paramMap = JSONUtilsEx.deserialize(paramJsonStr, Map.class);
        } else {

            paramMap = new HashMap<String, Object>();

            for (String paramName : (Set<String>) request.getParameterMap().keySet()) {

                if (StringUtils.isNotBlank(paramName)) {
                    paramMap.put(paramName, request.getParameter(paramName));
                }
            }
        }

        String ipAddress = SystemUtilsEx.getClientIpSingle(request);
        String userAgent = request.getHeader("user-agent");

        // header中加入：X-Ca-Timestamp，如果时间戳校验失败，返回当前时间
        // header中加入：X-Ca-Signature，签名 md5(timestamp,data,固定值)
        // 框架读取配置文件，如果设置了某个属性，则说明必须强制使用上面签名
        if (signclassMap.size() > 0) {

            Long nowTime = new Date().getTime();

            String timestamp = request.getHeader("X-Ca-Timestamp");

            String sign = request.getHeader("X-Ca-Signature");

            String appVersion = request.getHeader("appVersion");

            if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign)) {
                throw new ServiceException(MJSONResultEntity.REQUEST_SIGN_ERROR, "缺少签名信息");
            }

            try {
                if (Math.abs(nowTime - Long.valueOf(timestamp)) > 900000) {
                    throw new ServiceException(MJSONResultEntity.REQUEST_SIGN_TIME_ERROR, String.valueOf(nowTime));
                }
            } catch (NumberFormatException e) {
                logger.error("[安全检查]-签名时间错误,当前时间:{},签名时间:{},IP地址:{},userAgent:{}", new Object[]{nowTime, timestamp, ipAddress, userAgent});
                throw new ServiceException(MJSONResultEntity.REQUEST_SIGN_TIME_ERROR, "签名时间错误");
            }

            // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String paramStr = SignatureUtils.createLinkString(paramMap);

            IAPISign signMethod = !signclassMap.containsKey(appVersion) ? signclassMap.get("default") : signclassMap.get(appVersion);

            // 签名验证
            if (!signMethod.sign(appVersion, paramStr, timestamp, sign)) {
                logger.error("[安全检查]-签名错误,签名类:{},客户端签名:{},IP地址:{},userAgent:{},版本号:{}",
                        new Object[]{signMethod.getClass().getSimpleName(), sign, ipAddress, userAgent, appVersion});
                throw new ServiceException(MJSONResultEntity.REQUEST_SIGN_ERROR, "签名错误");
            }
        }

        // 分页处理
        if (paramMap.containsKey("page")) {

            if (!paramMap.containsKey("pagecount")) {
                throw new ServiceException("参数列表中存在参数page时,请同时传入分页参数:pagecount!");
            }

            // 手机端如果需要总页数,传入totalCount=null，如果不传则代表不需要总页数信息）(设置为0，这样不会再算一遍count)
            Integer totalCount = null;
            if (paramMap.get("totalcount") != null) {
                totalCount = Integer.valueOf(String.valueOf(paramMap.get("totalcount")));
            }

            PageLimitHolderFilter.setContext(Integer.valueOf(String.valueOf(paramMap.get("page"))),
                    Integer.valueOf(String.valueOf(paramMap.get("pagecount"))), totalCount);

            PageLimitHolderFilter.getContext().setOnlyGetRows("1".equals(paramMap.get("onlygetrows")));

            PageLimitHolderFilter.getContext().setLimited(true);
        }

        String appid = StringUtils.isNotBlank(CoreConstants.getProperty("application.appid"))
                ? CoreConstants.getProperty("application.appid") : (String) paramMap.get("appid");

        // 建立mobileInfo对象
        MobileInfo mobileInfo = new MobileInfo((String) paramMap.get("userId"), (String) paramMap.get("deviceid"), appid);
        paramMap.put("mobileInfo", mobileInfo);

        // 塞入线程变量
        LocalAttributeHolder.getContext().put("appid", appid);
        LocalAttributeHolder.getContext().put("userid", paramMap.get("userId"));
        LocalAttributeHolder.getContext().put("mobileInfo", mobileInfo);
        LocalAttributeHolder.getContext().put("ipAddress", ipAddress);

        if (!StringUtils.isBlank(userAgent)) {

            userAgent = userAgent.toLowerCase();

            String platform = "mobile";

            if (userAgent.indexOf("iphone") > -1) {
                platform = "iphone";
            } else if (userAgent.indexOf("ipad") > -1) {
                platform = "ipad";
            } else if (userAgent.indexOf("android") > -1 || userAgent.indexOf("apache-httpclient") > -1) {
                platform = "android";
            }
            LocalAttributeHolder.getContext().put("platform", platform);
        }

        // 如果传入参数是String类型，则做xss过滤
        for (String key : paramMap.keySet()) {
            if (paramMap.get(key) != null && paramMap.get(key) instanceof String) {
                paramMap.put(key, StringUtilsEx.escapeXss((String) paramMap.get(key)));
            }
        }

        return paramMap;
    }

    /**
     * 请求服务内容并反射后到protobuf
     *
     * @param methodName
     * @param openMethod
     * @param paramMap
     * @return
     * @throws Exception
     */
    private Object requestMethod(String methodName, OpenAPIMethod openMethod, Map<String, Object> paramMap) throws Exception {

        // 请求服务
        Method method = mobileMethodMap.get(methodName);

        // 开始支持分页，之前的信息查詢不需要分頁
        if (PageLimitHolderFilter.getContext() != null) {
            PageLimitHolderFilter.getContext().setLimited(false);
        }

        return ClassUtilsEx.invokeMethod(method.getDeclaringClass().getSimpleName(), method.getName(), paramMap);
    }

}
