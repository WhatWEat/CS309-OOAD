package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {
    private Long noticeId;
    private Long projectId;
    private String title;
    private String content;
    private Long creatorId;
    private LocalDateTime createTime;
    private int type; //FUNC:0是正常的notice，1是application，2是recruitment
    private int status; //FUNC:0是未决定，1是已同意，2是已拒绝
    private Long fromId;
    private Long groupId;

    @TableField(exist = false)
    private String fromName;

    @TableField(exist = false)
    private String groupName;

    @TableField(exist = false)
    private String creatorName;

    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    private List<Long> stuView;

    @TableField(exist = false)
    private Boolean toAll = false;

    public Notice(Long noticeId, Long projectId, String title, String content, Long creatorId) {
        this.noticeId = noticeId;
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.creatorId = creatorId;
    }

    public Notice(){
    }

    public Notice apply(Long fromId, Long groupId, Long projId){
        this.creatorId = null;
        this.fromId = fromId;
        this.groupId = groupId;
        this.projectId = projId;
        this.status = Status.UNDECIDED.ordinal();
        this.type = Type.APPLICATION.ordinal();
        this.createTime = LocalDateTime.now();
        return this;
    }

    public Notice recruit(Long fromId, Long groupId, Long projId){
        this.creatorId = null;
        this.fromId = fromId;
        this.groupId = groupId;
        this.projectId = projId;
        this.status = Status.UNDECIDED.ordinal();
        this.type = Type.RECRUITMENT.ordinal();
        this.createTime = LocalDateTime.now();
        return this;
    }

    public Notice agree(Long fromId, Long groupId, Long projId){
        this.fromId = fromId;
        this.groupId = groupId;
        this.projectId = projId;
        this.status = Status.AGREE.ordinal();
        return this;
    }

    public enum Type{
        NORMAL(0),
        APPLICATION(1),
        RECRUITMENT(2);

        Type(int i) {

        }
    }

    public enum Status{
        UNDECIDED(0),
        AGREE(1),
        REJECT(2);

        Status(int i) {

        }
    }

}
