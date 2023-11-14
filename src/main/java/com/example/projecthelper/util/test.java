package com.example.projecthelper.util;

public class test {
    public static void main(String[] args) {
        System.out.println(
            JWTUtil.verifyToken("eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIzMDAwMjAwMCIsImV4cCI6MTcwMDEyNzc5OSwiaWRlbnRpdHlDb2RlIjoiMSIsImlhdCI6MTY5OTk1NDk5OX0.WHPlzgKmoaYVDtLbkFcbh7ZwSVMwsdgGmZImjepCI-JCf7fXkkXBDDhTJYiHYHMsXq-FxoImlIoAo049SOTAu")
        );
        System.out.println(JWTUtil.verifyToken("eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIzMDAwMjAwMCIsImV4cCI6MTcwMDEyODAwOCwiaWRlbnRpdHlDb2RlIjoiMSIsImlhdCI6MTY5OTk1NTIwOH0.SuMbbHM97lRerNX2SZLNs1k3lL_YwaPJ1NCExdJrzEJ0oC3MQSFdNtYhm9M8PIcFiw5B7_w3E-ukoD1WfLsGsA"));
        System.out.println(generateValidJWT("30002000", "1"));

    }

    public static String generateValidJWT(String userId, String identityCode){
        return JWTUtil.createJWT(userId, identityCode);
    }
}
