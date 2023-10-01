package com.example.projecthelper.util.security;

import com.example.projecthelper.util.StatusCode;
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
    private int statusCode;
    private int identityCode;
    private T body;
    private String msg;
    private String JWT_token;

    public ResponseResult(int statusCode, int identityCode, T bd, String msg, String JWT_token) {
        this.statusCode = statusCode;
        this.identityCode = identityCode;
        this.body = bd;
        this.msg = msg;
        this.JWT_token = JWT_token;
    }

    public static <T> ResponseResult<T> ok(int identityCode, T bd, String msg, String JWT_token){
        return new ResponseResult<>(StatusCode.OK.getValue(), identityCode, bd, msg, JWT_token);
    }

    public static <T> ResponseResult<T> unAuthorize(T bd, String msg){
        return new ResponseResult<>(StatusCode.UNAUTHORIZED.getValue(), -1, bd, msg, "");
    }


}
