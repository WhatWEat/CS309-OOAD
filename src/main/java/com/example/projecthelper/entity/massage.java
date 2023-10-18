package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class massage {
    @TableField("massage_id")
    private long massageId;
    @TableField("from_id")
    private long fromId;
    @TableField("to_id")
    private long toId;
    private Timestamp chatTime;
    private String content;

    public massage(long massageId, long fromId, long toId, Timestamp chatTime, String content) {
        this.massageId = massageId;
        this.fromId = fromId;
        this.toId = toId;
        this.chatTime = chatTime;
        this.content = content;
    }

    public massage() {
    }
}
