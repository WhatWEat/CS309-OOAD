package com.example.projecthelper.security;

import com.example.projecthelper.service.LogoutBLKService;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.LogUtil;
import com.example.projecthelper.util.ResponseResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

public class BlacklistFilter extends OncePerRequestFilter {

    @Autowired
    private LogoutBLKService logoutBLKService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        // 获取请求的信息，例如IP地址或用户名
        if (HTTPUtil.requestSpecifiedPattern(HTTPUtil.IGNORE_PATTERN, request)){
            filterChain.doFilter(request, response);
            return;
        }
        System.err.println("blk");
        // 如果token无效，直接进行下一环
        String token = request.getHeader(HTTPUtil.TOKEN_HEADER);
        if(token == null)
            token = request.getHeader(HTTPUtil.ALTERNATE_TOKEN_HEADER);
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            Enumeration<String> headers = request.getHeaders(headerName);
//            while (headers.hasMoreElements()) {
//                String headerValue = headers.nextElement();
//                System.err.println(headerName + ": " + headerValue);
//            }
//        }
        if (Objects.isNull(token)){
            filterChain.doFilter(request,response);
            return;
        }
        if (!JWTUtil.verifyToken(token)) {
            LogUtil.log("invalid token" , LogUtil.WARN);
            filterChain.doFilter(request,response);
            return;
        }
        // 检查是否在黑名单中
        if (isBlacklisted(JWTUtil.getUserIdByToken(token))) {

            HTTPUtil.respondException(
                response,
                ResponseResult.unAuthorize(null, "您已登出请重新登录")
            );
        }
        // 不在黑名单，继续
        filterChain.doFilter(request, response);
    }

    private boolean isBlacklisted(String id) {
        // 实现你的黑名单检查逻辑，例如查询数据库或检查一个列表
        return logoutBLKService.isBlacklisted(id); // 示例返回值，实际应根据你的逻辑进行判断
    }
}

