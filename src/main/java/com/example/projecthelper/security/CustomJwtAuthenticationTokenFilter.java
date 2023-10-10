package com.example.projecthelper.security;

import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.JWTUtil;
import com.example.projecthelper.util.LogUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
@Primary
public class CustomJwtAuthenticationTokenFilter extends OncePerRequestFilter {
    public final static String AUTH_HEADER = "Token";

    @Autowired
    private UserDetailsService userDetailsService = new CustomUserDetailService();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
        ServletException, IOException {
        // get token from header:  Authorization: Bearer <token>
        String token = request.getHeader(AUTH_HEADER);
        if (Objects.isNull(token)){
            filterChain.doFilter(request,response);
            return;
        }

        LogUtil.log(token , LogUtil.INFO);
        //verify token
        if (!JWTUtil.verifyToken(token)) {
            LogUtil.log("invalid token" , LogUtil.INFO);
            filterChain.doFilter(request,response);
            return;
        }

        final String userId =  JWTUtil.getUserIdByToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        System.err.println(
            "here"
        );
        userDetails.getAuthorities().forEach(e -> System.err.println(e.getAuthority()));

        // 注意，这里使用的是3个参数的构造方法，此构造方法将认证状态设置为true
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        //将认证过了凭证保存到security的上下文中以便于在程序中使用
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
