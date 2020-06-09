package com.star.frame.component.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

import com.star.frame.component.api.IAPIVerify;
import com.star.frame.component.api.OpenAPINoneVerify;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenAPIMethod {
    
    // 对应的应用名,默认为当前方法名.函数名
    String methodName() default StringUtils.EMPTY;
    
    // 对应的servlet名称,默认为当前方法名.函数名
    String servletName() default StringUtils.EMPTY;
    
    // 对应的head中的SC-version名称,默认为当前""
    String version() default StringUtils.EMPTY;
    
    // 需要登录的页面，默认为true
    // boolean isLogin();
    
    Class<? extends IAPIVerify> verify() default OpenAPINoneVerify.class;
    
}
