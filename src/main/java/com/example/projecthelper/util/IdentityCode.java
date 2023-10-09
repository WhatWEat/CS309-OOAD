package com.example.projecthelper.util;

import lombok.Getter;
import org.apache.tomcat.util.bcel.Const;

@Getter
public enum IdentityCode {
    ADMINISTRATOR(0),
    TEACHER(1),
    TEACHER_ASSISTANT(2),
    STUDENT(3),
    UNDEFINED(4);

    private final int value;

    IdentityCode(int value) {
        this.value = value;
    }

    public static IdentityCode getICByName(String s){
        return switch (s) {
            case "ADMINISTRATOR" -> ADMINISTRATOR;
            case "TEACHER" -> TEACHER;
            case "TEACHER_ASSISTANT" -> TEACHER_ASSISTANT;
            case "STUDENT" -> STUDENT;
            default -> UNDEFINED;
        };
    }

    public static IdentityCode getICByCode(int code){
        return switch (code) {
            case 0 -> ADMINISTRATOR;
            case 1 -> TEACHER;
            case 2 -> TEACHER_ASSISTANT;
            case 3 -> STUDENT;
            default -> UNDEFINED;
        };
    }
}
