package com.example.projecthelper.security;

import com.example.projecthelper.util.JsonUtil;
import com.example.projecthelper.util.ResponseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws
        IOException, ServletException {
        System.err.println(Arrays.toString(accessDeniedException.getStackTrace()));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(JsonUtil.serialize(ResponseResult.accessDenied(null, "权限不够")));
        response.getWriter().flush();
    }
}
