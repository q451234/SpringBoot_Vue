package com.sensor.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(){
        return new Result<>(Constant.SUCCESS_CODE,Constant.SUCCESS_MESSAGE,null);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(Constant.SUCCESS_CODE,Constant.SUCCESS_MESSAGE,data);
    }

    public static <T> Result<T> success(T data,String message){
        return new Result<>(Constant.SUCCESS_CODE,message,data);
    }

    public static <T> Result<T> success(String message){
        return new Result<>(Constant.SUCCESS_CODE,message,null);
    }

    public static<T>  Result<T> fail(Integer code, String message){
        return new Result<>(code,message,null);
    }


}
