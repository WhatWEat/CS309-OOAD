package com.example.projecthelper.util;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

    /**
     * 将一个对象序列化为JSON字符串
     * @param object 要序列化的对象
     * @return JSON字符串
     */
    public static String serialize(Object object) {
        return JSON.toJSONString(object);
    }

    // 如果您需要其他JSON相关的实用方法，可以在此类中进一步添加
}
