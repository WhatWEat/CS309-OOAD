package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class massage {
    private Long massageId;
    private Long fromId;
    private Long toId;
    private Timestamp chatTime;
    private String content;

    public massage(Long massageId, Long fromId, Long toId, Timestamp chatTime, String content) {
        this.massageId = massageId;
        this.fromId = fromId;
        this.toId = toId;
        this.chatTime = chatTime;
        this.content = content;
    }

    public massage() {
    }
}
