package com.example.projecthelper.util;



public class HTTPUtil {
    public static final String TOKEN_HEADER = "Token";

    public static String getHeader(jakarta.servlet.http.HttpServletRequest request, String HeaderName) {
        return request.getHeader(HeaderName);
    }

    public static String getHeader(javax.servlet.http.HttpServletRequest request, String HeaderName) {
        return request.getHeader(HeaderName);
    }
}

