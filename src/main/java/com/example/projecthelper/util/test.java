package com.example.projecthelper.util;

import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


public class test {
    public static void main(String[] args) {
        System.out.println(JWTUtil.getUserIdByToken("eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIxMjExMDAxMyIsImV4cCI6MTcwMTg3NDQzMiwiaWRlbnRpdHlDb2RlIjoiMyIsImlhdCI6MTcwMTcwMTYzMn0.nBab1OmulIot8IHpuuNfS2IXnuyLiNjAIFDJVzVYB4bAIJvfInQS-VyWnRUciAETNOf4fC2PoUoG3ObCRLwr-Q"));
        System.out.println(generateValidJWT("12110013", "3"));
    }

    public static String generateValidJWT(String userId, String identityCode){
        return JWTUtil.createJWT(userId, identityCode);
    }
}
enum Type{
    NORMAL(0),
    APPLICATION(1),
    RECRUITMENT(2);

    Type(int i) {
    }
}
