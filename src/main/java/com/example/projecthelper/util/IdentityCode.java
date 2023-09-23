package com.example.projecthelper.util;

import lombok.Getter;

@Getter
public enum IdentityCode {
    ADMINISTRATOR(0),
    TEACHER(1),
    STUDENT(2),
    TEACHER_ASSISTANT(3),
    GROUP_LEADER(4);

    private final int value;

    IdentityCode(int value) {
        this.value = value;
    }


}
