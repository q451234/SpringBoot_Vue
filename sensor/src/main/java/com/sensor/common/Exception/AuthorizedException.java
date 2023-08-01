package com.sensor.common.Exception;

/**
 * 自定义权限异常类
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
