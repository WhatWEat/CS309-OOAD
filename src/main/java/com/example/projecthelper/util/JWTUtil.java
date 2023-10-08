package com.example.projecthelper.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final int ttlMills = 60 * 60;
    private static final String secret = "CS309-OOAD-ProjectHelper";
    static Map<String, Object> headerClaims;
    static {
        headerClaims = new HashMap<>();
        headerClaims.put("type", "JWT");
    }

    // 修改createJWT方法以接受userId和identityCode
    public static String createJWT(String userId, String identityCode) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        return JWT.create()
            .withHeader(headerClaims)
            .withSubject(userId)  // 设置userId为JWT的subject
            .withClaim("identityCode", identityCode)  // 添加一个名为identityCode的claim
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * ttlMills))
            .sign(algorithm);
    }

    public static String updateJWT(String token) {
        String userId = getUserIdByToken(token);
        String identityCode = getIdentityCodeByToken(token);

        Algorithm algorithm = Algorithm.HMAC512(secret);
        return JWT.create()
            .withHeader(headerClaims)
            .withSubject(userId)  // 设置userId为JWT的subject
            .withClaim("identityCode", identityCode)  // 添加一个名为identityCode的claim
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * ttlMills))
            .sign(algorithm);
    }

    // 解析token以获取userId
    public static String getUserIdByToken(String token) throws JWTDecodeException {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    // 解析token以获取identityCode
    public static String getIdentityCodeByToken(String token) throws JWTDecodeException {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("identityCode").asString();
    }
}
