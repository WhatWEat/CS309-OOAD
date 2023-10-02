package com.example.projecthelper.entity;

import java.sql.Time;
import java.sql.Timestamp;
import lombok.Getter;

@Getter
public class Chat {
    private final Integer chat_id;
    private final Integer from_id;
    private final Integer to_id;
    private final Timestamp chatTime;

    private final String content;

    public Chat(Integer chat_id, Integer from_id, Integer to_id, Timestamp chatTime, String content) {
        this.chat_id = chat_id;
        this.from_id = from_id;
        this.to_id = to_id;
        this.chatTime = chatTime;
        this.content = content;
    }
}
