package com.example.projecthelper.entity;

import java.sql.Time;
import java.sql.Timestamp;

public class chat {
    private final Integer chat_id;
    private final Integer from_id;
    private final Integer to_id;
    private final Timestamp chatTime;

    private final String content;

    public chat(Integer chat_id, Integer from_id, Integer to_id, Timestamp chatTime, String content) {
        this.chat_id = chat_id;
        this.from_id = from_id;
        this.to_id = to_id;
        this.chatTime = chatTime;
        this.content = content;
    }

    public Integer getChat_id() {
        return chat_id;
    }

    public Integer getFrom_id() {
        return from_id;
    }

    public Integer getTo_id() {
        return to_id;
    }

    public Timestamp getChatTime() {
        return chatTime;
    }

    public String getContent() {
        return content;
    }
}
