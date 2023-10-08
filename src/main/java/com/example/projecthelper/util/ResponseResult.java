package com.example.projecthelper.util;

import com.fasterxml .jackson .annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * singleton模式
 * @param <T>
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T>{
    private int statusCode; //状态码
    private T body; //响应体
    private String msg; //响应信息
    private String JWT_token;

    public ResponseResult(int statusCode, T bd, String msg, String JWT_token) {
        this.statusCode = statusCode;
        this.body = bd;
        this.msg = msg;
        this.JWT_token = JWT_token;
    }

    public static <T> ResponseResult<T> ok(T bd, String msg, String JWT_token){
        return new ResponseResult<>(StatusCode.OK.getValue(), bd, msg, JWT_token);
    }

    public static <T> ResponseResult<T> unAuthorize(T bd, String msg){
        return new ResponseResult<>(StatusCode.UNAUTHORIZED.getValue(), bd, msg, "");
    }


}
