package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat {
    @TableField("chat_id")
    private long chatId;
    @TableField("user1_id")
    private long user1Iid;
    @TableField("user2_id")
    private long user2Id;

    public Chat(long chatId, long user1Iid, long user2Id) {
        this.chatId = chatId;
        this.user1Iid = user1Iid;
        this.user2Id = user2Id;
    }

    public Chat() {
    }
}
