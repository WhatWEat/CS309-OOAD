package com.example.projecthelper.util.Wrappers;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KeyValueWrapper<K, V> {
    K key;
    V value;
    public KeyValueWrapper() {

    }
    public KeyValueWrapper(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
