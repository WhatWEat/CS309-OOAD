package com.example.projecthelper.util.Wrappers;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyValueWrapper<K, V> {
    K key;
    V value;
}
