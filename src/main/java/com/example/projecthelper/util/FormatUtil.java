package com.example.projecthelper.util;

import java.util.function.Predicate;
import java.util.regex.Pattern;

//这是一个进行格式检查的类
public class FormatUtil {
    public static Predicate<String> strongPasswordPredicate(){
        //至少包含一个数字，大小写字母和特殊符号
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$");
        return str -> pattern.matcher(str).matches();
    }

    public static Predicate<String> lengthPredicate(int minLen, int maxLen){
        return str -> str.length() <= maxLen && minLen <= str.length();
    }
    public static <T> boolean match(T item, Predicate<T> predicate){
        return predicate.test(item);
    }
}
