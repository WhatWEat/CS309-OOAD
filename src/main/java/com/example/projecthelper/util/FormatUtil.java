package com.example.projecthelper.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

//这是一个进行格式检查的类
public class FormatUtil {

    public static Predicate<String> userIdPredicate(){
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        return str -> pattern.matcher(str).matches();
    }
    public static Predicate<String> strongPasswordPredicate(){
        //至少包含一个数字，大小写字母和特殊符号
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$");
        return str -> pattern.matcher(str).matches();
    }

    /**
     * 左闭右开
     * @param lb 下界
     * @param ub 上界
     * @param comparator T的比较器
     * @return 判断器
     * @param <T> 任意类
     */
    public static <T> Predicate<T> inARange(T lb, T ub, Comparator<T> comparator){
        return item -> comparator.compare(item, lb) >= 0 && comparator.compare(item, ub) < 0;
    }

    /**
     * 左闭右开
     * @param lb 下界
     * @param ub 上界
     * @return 判断器
     * @param <T> 实现Comparable的任意类
     */
    public static <T extends Comparable<T>> Predicate<T> inARange(T lb, T ub){
        return item -> item.compareTo(lb) >= 0 && item.compareTo(ub) < 0;
    }

    /**
     *
     * @param collection 集合
     * @return 判断器
     * @param <T> 参数
     */
    public static <T> Predicate<T> inCollection(Collection<T> collection){
        return item -> new HashSet<T>(collection).contains(item);
    }

    /**
     *
     * @param item 条目
     * @param predicate 判断器
     * @return 是否满足条件
     * @param <T> 参数
     */
    public static <T> boolean match(T item, Predicate<T> predicate){
        return predicate.test(item);
    }
}
