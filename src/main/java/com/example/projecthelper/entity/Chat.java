package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Chat {
    @TableField("chat_id")
    private long chatId;
    @TableField("from_id")
    private long fromId;
    @TableField("to_id")
    private long toId;
    private Timestamp chatTime;

    private String content;

    public Chat(long chatId, long fromId, long toId, Timestamp chatTime, String content) {
        this.chatId = chatId;
        this.fromId = fromId;
        this.toId = toId;
        this.chatTime = chatTime;
        this.content = content;
    }

    public Chat() {
    }
}
