package com.example.projecthelper.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat {
    private final long chat_id;
    private final long from_id;
    private final long to_id;
    private final Timestamp chatTime;

    private final String content;

    public Chat(long chat_id, long from_id, long to_id, Timestamp chatTime, String content) {
        this.chat_id = chat_id;
        this.from_id = from_id;
        this.to_id = to_id;
        this.chatTime = chatTime;
        this.content = content;
    }

}
