/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package com.star.frame.component.status.logs.tools;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardWrapper;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import com.star.frame.component.status.logs.beans.ContainerWrapperBean;
import com.star.frame.component.status.logs.model.Application;
import com.star.frame.component.status.logs.model.ApplicationParam;
import com.star.frame.component.status.logs.model.ApplicationSession;
import com.star.frame.component.status.logs.model.Attribute;
import com.star.frame.component.status.logs.model.FilterInfo;
import com.star.frame.component.status.logs.model.ServletInfo;
import com.star.frame.component.status.logs.model.ServletMapping;

/**
 * The Class ApplicationUtils.
 */
public final class ApplicationUtils {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ApplicationUtils.class);

  /**
   * Prevent Instantiation.
   */
  private ApplicationUtils() {
    // Prevent Instantiation
  }
  
  /**
   * Calculates Sum of requestCount, errorCount and processingTime for all servlets for the given
   * application. It also works out minimum value of minTime and maximum value for maxTime for all
   * servlets.
   *
   * @param context the context whose stats will be collected
   * @param app the application in which to store the collected stats
   */
  public static void collectApplicationServletStats(Context context, Application app) {
    int svltCount = 0;
    int reqCount = 0;
    int errCount = 0;
    long procTime = 0;
    long minTime = Long.MAX_VALUE;
    long maxTime = 0;

    for (Container container : context.findChildren()) {
      if (container instanceof StandardWrapper) {
        StandardWrapper sw = (StandardWrapper) container;
        svltCount++;
        reqCount += sw.getRequestCount();
        errCount += sw.getErrorCount();
        procTime += sw.getProcessingTime();
        if (sw.getRequestCount() > 0) {
          minTime = Math.min(minTime, sw.getMinTime());
        }
        maxTime = Math.max(maxTime, sw.getMaxTime());
      }
    }
    app.setServletCount(svltCount);
    app.setRequestCount(reqCount);
    app.setErrorCount(errCount);
    app.setProcessingTime(procTime);
    app.setMinTime(minTime == Long.MAX_VALUE ? 0 : minTime);
    app.setMaxTime(maxTime);
  }

  /**
   * Gets the application session.
   *
   * @param session the session
   * @param calcSize the calc size
   * @param addAttributes the add attributes
   * @return the application session
   */
  public static ApplicationSession getApplicationSession(Session session, boolean calcSize,
      boolean addAttributes) {

    ApplicationSession sbean = null;
    if (session != null && session.isValid()) {
      sbean = new ApplicationSession();

      sbean.setId(session.getId());
      sbean.setCreationTime(new Date(session.getCreationTime()));
      sbean.setLastAccessTime(new Date(session.getLastAccessedTime()));
      sbean.setMaxIdleTime(session.getMaxInactiveInterval() * 1000);
      sbean.setManagerType(session.getManager().getClass().getName());
      // sbean.setInfo(session.getInfo());
      // TODO:fixmee

      boolean sessionSerializable = true;
      int attributeCount = 0;
      long size = 0;

      HttpSession httpSession = session.getSession();
      Set<Object> processedObjects = new HashSet<>(1000);

      // Exclude references back to the session itself
      processedObjects.add(httpSession);
      try {
        for (String name : Collections.list(httpSession.getAttributeNames())) {
          Object obj = httpSession.getAttribute(name);
          sessionSerializable = sessionSerializable && obj instanceof Serializable;

          long objSize = 0;
          if (calcSize) {
            try {
              objSize += Instruments.sizeOf(name, processedObjects);
              objSize += Instruments.sizeOf(obj, processedObjects);
            } catch (Exception ex) {
              logger.error("Cannot estimate size of attribute '{}'", name, ex);
            }
          }

          if (addAttributes) {
            Attribute saBean = new Attribute();
            saBean.setName(name);
            saBean.setType(ClassUtils.getQualifiedName(obj.getClass()));
            saBean.setValue(obj);
            saBean.setSize(objSize);
            saBean.setSerializable(obj instanceof Serializable);
            sbean.addAttribute(saBean);
          }
          attributeCount++;
          size += objSize;
        }
        String lastAccessedIp =
            (String) httpSession.getAttribute(ApplicationSession.LAST_ACCESSED_BY_IP);
        if (lastAccessedIp != null) {
          sbean.setLastAccessedIp(lastAccessedIp);
        }
        try {
/*          sbean.setLastAccessedIpLocale(InetAddressLocator.getLocale(InetAddress.getByName(
              lastAccessedIp).getAddress()));*/
        } catch (Exception e) {
          logger.error("Cannot determine Locale of {}", lastAccessedIp);
          logger.trace("", e);
        }


      } catch (IllegalStateException e) {
        logger.info("Session appears to be invalidated, ignore");
        logger.trace("", e);
      }

      sbean.setObjectCount(attributeCount);
      sbean.setSize(size);
      sbean.setSerializable(sessionSerializable);
    }

    return sbean;
  }

  /**
   * Gets the application attributes.
   *
   * @param context the context
   * @return the application attributes
   */
  public static List<Attribute> getApplicationAttributes(Context context) {
    List<Attribute> attrs = new ArrayList<>();
    ServletContext servletCtx = context.getServletContext();
    for (String attrName : Collections.list(servletCtx.getAttributeNames())) {
      Object attrValue = servletCtx.getAttribute(attrName);

      Attribute attr = new Attribute();
      attr.setName(attrName);
      attr.setValue(attrValue);
      attr.setType(ClassUtils.getQualifiedName(attrValue.getClass()));
      attrs.add(attr);
    }
    return attrs;
  }

  /**
   * Gets the application init params.
   *
   * @param context the context
   * @param containerWrapper the container wrapper
   * @return the application init params
   */
  public static List<ApplicationParam> getApplicationInitParams(Context context,
      ContainerWrapperBean containerWrapper) {

    return containerWrapper.getTomcatContainer().getApplicationInitParams(context);
  }

  /**
   * Gets the application servlet.
   *
   * @param context the context
   * @param servletName the servlet name
   * @return the application servlet
   */
  public static ServletInfo getApplicationServlet(Context context, String servletName) {
    Container container = context.findChild(servletName);

    if (container instanceof Wrapper) {
      Wrapper wrapper = (Wrapper) container;
      return getServletInfo(wrapper, context.getName());
    }
    return null;
  }

  /**
   * Gets the servlet info.
   *
   * @param wrapper the wrapper
   * @param contextName the context name
   * @return the servlet info
   */
  private static ServletInfo getServletInfo(Wrapper wrapper, String contextName) {
    ServletInfo si = new ServletInfo();
    si.setApplicationName(contextName.length() > 0 ? contextName : "/");
    si.setServletName(wrapper.getName());
    si.setServletClass(wrapper.getServletClass());
    si.setAvailable(!wrapper.isUnavailable());
    si.setLoadOnStartup(wrapper.getLoadOnStartup());
    si.setRunAs(wrapper.getRunAs());
    si.getMappings().addAll(Arrays.asList(wrapper.findMappings()));
    if (wrapper instanceof StandardWrapper) {
      StandardWrapper sw = (StandardWrapper) wrapper;
      si.setAllocationCount(sw.getCountAllocated());
      si.setErrorCount(sw.getErrorCount());
      si.setLoadTime(sw.getLoadTime());
      si.setMaxInstances(sw.getMaxInstances());
      si.setMaxTime(sw.getMaxTime());
      si.setMinTime(sw.getMinTime() == Long.MAX_VALUE ? 0 : sw.getMinTime());
      si.setProcessingTime(sw.getProcessingTime());
      si.setRequestCount(sw.getRequestCount());
      // Tomcat 7.0.72+, 8.0.37+, 8.5.5+, and 9.0.0.M10 modified from boolean to Boolean.
      // Since SingleThreadModel deprecated in servlet 2.4 with no direct replacement,
      // we will continue to handle as boolean. Previously calling this would have
      // resulted in class being loaded if not already. This is why Null is returned
      // now.
      try {
        Object singleThreaded = MethodUtils.invokeMethod(sw, "isSingleThreadModel", null);
        if (singleThreaded == null) {
          si.setSingleThreaded(false);
        } else {
          si.setSingleThreaded(Boolean.parseBoolean(String.valueOf(singleThreaded)));
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        logger.error("Cannot determine single threading");
        logger.trace("", e);
      }
    }
    return si;
  }

  /**
   * Gets the application servlets.
   *
   * @param context the context
   * @return the application servlets
   */
  public static List<ServletInfo> getApplicationServlets(Context context) {
    Container[] cns = context.findChildren();
    List<ServletInfo> servlets = new ArrayList<>(cns.length);
    for (Container container : cns) {
      if (container instanceof Wrapper) {
        Wrapper wrapper = (Wrapper) container;
        servlets.add(getServletInfo(wrapper, context.getName()));
      }
    }
    return servlets;
  }

  /**
   * Gets the application servlet maps.
   *
   * @param context the context
   * @return the application servlet maps
   */
  public static List<ServletMapping> getApplicationServletMaps(Context context) {
    String[] sms = context.findServletMappings();
    List<ServletMapping> servletMaps = new ArrayList<>(sms.length);
    for (String servletMapping : sms) {
      if (servletMapping != null) {
        String sn = context.findServletMapping(servletMapping);
        if (sn != null) {
          ServletMapping sm = new ServletMapping();
          sm.setApplicationName(context.getName().length() > 0 ? context.getName() : "/");
          sm.setUrl(servletMapping);
          sm.setServletName(sn);
          Container container = context.findChild(sn);
          if (container instanceof Wrapper) {
            Wrapper wrapper = (Wrapper) container;
            sm.setServletClass(wrapper.getServletClass());
            sm.setAvailable(!wrapper.isUnavailable());
          }
          servletMaps.add(sm);
        }
      }
    }
    return servletMaps;
  }

  /**
   * Gets the application filters.
   *
   * @param context the context
   * @param containerWrapper the container wrapper
   * @return the application filters
   */
  public static List<FilterInfo> getApplicationFilters(Context context, ContainerWrapperBean containerWrapper) {
    return containerWrapper.getTomcatContainer().getApplicationFilters(context);
  }

}
