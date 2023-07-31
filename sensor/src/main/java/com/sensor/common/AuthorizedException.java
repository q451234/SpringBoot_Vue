package com.sensor.common;

/**
 * 自定义异常类
 */
public class AuthorizedException extends Exception {
    //异常信息
    private String message;

    //构造函数
    public AuthorizedException(String message){
        super(message);
        this.message = message;
    }
}
