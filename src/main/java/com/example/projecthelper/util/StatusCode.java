package com.example.projecthelper.util;

import lombok.Getter;

@Getter
public enum StatusCode {

    OK(200),
    LOGOUT(201),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    ACCESS_DENIED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INVALID_CONTENT(405),
    INTERNAL_SERVER_ERROR(500);
    // ... 可根据需要继续添加其他状态码

    private final int value;

    StatusCode(int value) {
        this.value = value;
    }

}
