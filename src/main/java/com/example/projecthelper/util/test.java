package com.example.projecthelper.util;

public class test {
    public static void main(String[] args) {
        System.out.println(generateValidJWT("3", "3"));

    }

    public static String generateValidJWT(String userId, String identityCode){
        return JWTUtil.createJWT(userId, identityCode);
    }
}
