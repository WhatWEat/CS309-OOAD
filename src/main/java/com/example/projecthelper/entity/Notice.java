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
    protected Long noticeId;
    protected Long projectId;
    protected String title;
    protected String content;
    protected Long creatorId;
    protected LocalDateTime createTime;
    protected int type; //FUNC:0是正常的notice，1是application，2是recruitment
    protected int status; //FUNC:0是未决定，1是已同意，2是已拒绝
    protected Long groupId;

    @TableField(exist = false)
    protected String fromName;

    @TableField(exist = false)
    protected String groupName;

    @TableField(exist = false)
    protected String creatorName;

    @TableField(exist = false)
    protected String projectName;

    @TableField(exist = false)
    protected List<Long> stuView;

    @TableField(exist = false)
    protected List<String> stuViewName;

    @TableField(exist = false)
    protected Boolean toAll = false;


    public Notice(Long noticeId, Long projectId, String title, String content, Long creatorId) {
        this.noticeId = noticeId;
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.creatorId = creatorId;
    }

    public Notice(){
    }

//    public Notice apply(Long fromId, Long groupId, Long projId){
//        this.creatorId = null;
//        this.fromId = fromId;
//        this.groupId = groupId;
//        this.projectId = projId;
//        this.status = Status.UNDECIDED.ordinal();
//        this.type = Type.APPLICATION.ordinal();
//        this.createTime = LocalDateTime.now();
//        return this;
//    }
//
//    public Notice recruit(Long fromId, Long groupId, Long projId){
//        this.creatorId = null;
//        this.fromId = fromId;
//        this.groupId = groupId;
//        this.projectId = projId;
//        this.status = Status.UNDECIDED.ordinal();
//        this.type = Type.RECRUITMENT.ordinal();
//        this.createTime = LocalDateTime.now();
//        return this;
//    }

//    public Notice agree(Long fromId, Long groupId, Long projId){
//        this.fromId = fromId;
//        this.groupId = groupId;
//        this.projectId = projId;
//        this.status = Status.AGREE.ordinal();
//        return this;
//    }

    @Getter
    public enum Type{
        NORMAL(0),
        APPLICATION(1),
        RECRUITMENT(2);

        private final int value;

        Type(int i) {
            value = i;
        }
    }

    @Getter
    public enum Status{
        EXPIRED(-1),
        UNDECIDED(0),
        AGREE(1),
        REJECT(2);

        private final int value;

        Status(int i) {
            value = i;
        }

    }

}
