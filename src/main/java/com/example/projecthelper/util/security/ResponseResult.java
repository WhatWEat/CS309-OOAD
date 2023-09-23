package com.example.projecthelper.util.security;

import com.example.projecthelper.util.StatusCode;
import com.fasterxml .jackson .annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult{
    private int statusCode;
    private int identityCode;
    private String msg;
    private String JWT_token;

    public ResponseResult(int statusCode, int identityCode, String msg, String JWT_token) {
        this.statusCode = statusCode;
        this.identityCode = identityCode;
        this.msg = msg;
    }

    public static ResponseResult ok(int identityCode, String msg, String JWT_token){
        return new ResponseResult(StatusCode.OK.getValue(), identityCode, msg, JWT_token);
    }

    public static ResponseResult unAuthorize(String msg){
        return new ResponseResult(StatusCode.UNAUTHORIZED.getValue(), -1, msg, "");
    }


}
