package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat {
    private Long chatId;
    private Long user1Iid;
    private Long user2Id;

    public Chat(Long chatId, Long user1Iid, Long user2Id) {
        this.chatId = chatId;
        this.user1Iid = user1Iid;
        this.user2Id = user2Id;
    }

    public Chat() {
    }
}
