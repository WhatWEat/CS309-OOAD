package com.example.projecthelper.util;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class HTTPUtil {
    public static final String TOKEN_HEADER = "Token";

    public static final String [] IGNORE_PATTERN = {
        "/login", "/logout", "/signup",
        "/request_code", "/request_massage",
        "/login_with_email_code","/login_with_message_code",
        "/get_forget_password_code", "/change_forget_password"
    };


    public static final String [] REQUEST_CODE = {"/request_code","/request_massage"};

    public static String getHeader(jakarta.servlet.http.HttpServletRequest request, String HeaderName) {
        return request.getHeader(HeaderName);
    }

    public static String getHeader(javax.servlet.http.HttpServletRequest request, String HeaderName) {
        return request.getHeader(HeaderName);
    }

    public static String declareAttachment(String filename){
        return "attachment; filename=\"" + filename + "\"";
    }

    public static void respondException(HttpServletResponse response, ResponseResult <?> rr)
        throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(rr.getStatusCode());
        response.getWriter().write(JsonUtil.serialize(rr));
        response.getWriter().flush();
    }

    public static boolean requestSpecifiedPattern(String [] patterns, HttpServletRequest request){
        return new HashSet<>(Arrays.asList(patterns)).contains(request.getRequestURI());
    }
}

