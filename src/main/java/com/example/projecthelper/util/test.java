package com.example.projecthelper.util;

public class test {
    public static void main(String[] args) {
        String token = JWTUtil.createJWT("10", "abc");
        System.out.println(JWTUtil.getUserIdByToken("eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJudWxsIiwiZXhwIjoxNjk2OTQ3NTc1LCJpZGVudGl0eUNvZGUiOiIwIiwiaWF0IjoxNjk2OTQ2Njc1fQ.wQeOGBrXQwjgVK2Y9hy8ekci30SlvJERnZqgCy7V9j2e66bXW3Ktljm9dq07G8D8Tc8vyIaI5q0bD2uN68FjMw"));
    }
}
