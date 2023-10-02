package com.example.projecthelper.util;

import lombok.Getter;

@Getter
public enum IdentityCode {
    ADMINISTRATOR(0),
    TEACHER(1),
    TEACHER_ASSISTANT(2),
    STUDENT(3),
    GROUP_LEADER(4);

    private final int value;

    IdentityCode(int value) {
        this.value = value;
    }


}
