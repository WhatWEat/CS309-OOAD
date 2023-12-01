package com.example.projecthelper.config;

import com.example.projecthelper.security.BlacklistFilter;
import com.example.projecthelper.security.CustomJwtAuthenticationTokenFilter;
import com.example.projecthelper.security.JwtAuthenticationProvider;
import com.example.projecthelper.security.UnauthorizedHandler;
import com.example.projecthelper.util.HTTPUtil;
import com.example.projecthelper.util.IdentityCode;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 创建一些WebSecurity必须的bean
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UnauthorizedHandler unauthorizedHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //我们自定义的拦截器，只要JWT验证成功，直接通过，不再要求用户密码
    @Bean
    public CustomJwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new CustomJwtAuthenticationTokenFilter();
    }

    @Bean
    public BlacklistFilter blackListFilter(){
        return new BlacklistFilter();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthProvider(){
        return new JwtAuthenticationProvider();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        //自动加盐的单向密码加密器
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers(HTTPUtil.IGNORE_PATTERN);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //使用自定义provider
        httpSecurity
            .authenticationProvider(jwtAuthProvider());

        httpSecurity
            //添加JWT filter
            .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            // 添加黑名单filter
            .addFilterBefore(blackListFilter(), jwtAuthenticationTokenFilter().getClass());

        httpSecurity
            .exceptionHandling((exception)->
                exception.authenticationEntryPoint(unauthorizedHandler)) //处理认证异常（账号密码错误）
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.accessDeniedHandler(accessDeniedHandler) //处理权限异常
            );

        httpSecurity
            .authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                    .requestMatchers("/adm/**").hasAuthority(IdentityCode.ADMINISTRATOR.name())
                    .requestMatchers("/tea/**").hasAuthority(IdentityCode.TEACHER.name())
                    .requestMatchers("/ta/**").hasAuthority(IdentityCode.TEACHER_ASSISTANT.name())
                    .requestMatchers("/stu/**").hasAuthority(IdentityCode.STUDENT.name())
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
            );

        httpSecurity
            .csrf(AbstractHttpConfigurer::disable);


        return httpSecurity.build();
    }

}
