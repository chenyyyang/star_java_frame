package com.star.frame.core.support.exception;

public class ServiceException extends RuntimeException {
    
    private String code;
    
    private Object data;
    
    public ServiceException() {
    }
    
    public ServiceException(Throwable e) {
        super(e);
    }
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public ServiceException(Integer code, String message) {
        super(message);
        this.code = String.valueOf(code);
    }
    
    public ServiceException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }
    
    public ServiceException(String message, Throwable e) {
        super(message, e);
    }
    
    public String getCode() {
        return code;
    }
    
    public Object getData() {
        return data;
    }
    
    /**
     * 如果存在code,但是无text,且定义了message_i18n,则从message中取得具体的text值
     */
    @Override
    public String getMessage() {
        
        return super.getMessage();
    }
}
