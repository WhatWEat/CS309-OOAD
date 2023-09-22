package com.example.projecthelper.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JWTUtil {

    private static final int ttlMills = 60 * 60;
    private static final String secret = "CS309--OOAD-ProjectHelper";
    //createJWT方法
    public static String createJWT(String str){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * ttlMills);
        return Jwts.builder()
            .setHeaderParam("type", "JWT")
            .setSubject(str)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
    //解析token
    public static String getClaimsByToken(String token){
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }


}
