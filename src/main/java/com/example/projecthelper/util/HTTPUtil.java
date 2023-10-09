package com.example.projecthelper.util;

import javax.servlet.http.HttpServletRequest;

public class HTTPUtil {

    public static String getHeader(HttpServletRequest request, String HeaderName) {
        return request.getHeader(HeaderName);
    }
}

