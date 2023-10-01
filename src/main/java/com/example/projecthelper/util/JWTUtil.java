package com.example.projecthelper.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JWTUtil {

    private static final int ttlMills = 60 * 60;
    private static final String secret = "CS309-OOAD-ProjectHelper";

    // 修改createJWT方法以接受userId和identityCode
    public static String createJWT(String userId, String identityCode) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * ttlMills);
        return Jwts.builder()
            .setHeaderParam("type", "JWT")
            .setSubject(userId)   // 设置userId为JWT的subject
            .claim("identityCode", identityCode)   // 添加一个名为identityCode的claim
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    // 解析token以获取userId
    public static String getUserIdByToken(String token) throws JwtException {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    // 解析token以获取identityCode
    public static String getIdentityCodeByToken(String token) throws JwtException {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody()
            .get("identityCode", String.class);   // 注意，这里我们明确指定了返回的类型为String
    }
}
