package com.example.projecthelper.util.Wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectCountWrapper<T> {
    private T obj;
    private int count;
}
