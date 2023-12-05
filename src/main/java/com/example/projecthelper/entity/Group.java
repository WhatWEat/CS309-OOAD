package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("groups")
public class Group implements Cloneable{
    private Long groupId;
    private String groupName;
    private Long creatorId;
    private Long instructorId;
    @TableField(exist = false)
    private String instructorName;
    private Long leaderId;
    @TableField(exist = false)
    private String leaderName;
    private Long maxsize;
    private Long projectId;
    private LocalDateTime teamTime;
    private LocalDateTime deadline;
    private LocalDateTime reportTime;
    private String description;
    private List<String> technicalStack;
    private List<Boolean> visibility;
    private String recruitment;
    @TableField(exist = false)
    private List<Long> memberIds;
    @TableField(exist = false)
    private List<String> members;
    @TableField(exist = false)
    private int memCnt;


    public Group(Long groupId,Long instructorId, String groupName, Long maxsize, Long projectId) {
        this.groupId = groupId;
        this.instructorId = instructorId;
        this.groupName = groupName;
        this.maxsize = maxsize;
        this.projectId = projectId;
    }

//    public Group(String groupName, Long projectId, String description, List<String> technicalStack, List<Boolean> visibility) {
//        this.groupName = groupName;
//        this.projectId = projectId;
//        this.description = description;
//        this.technicalStack = technicalStack;
//        this.visibility = visibility;
//    }

    public Group() {
    }

    public Group mask(){
        if(!visibility.get(0)){
            this.members = null;
            this.memberIds = null;
        }
        if(!visibility.get(1)){
            leaderId = null;
            leaderName = null;
        }
        if(!visibility.get(2)){
            teamTime = null;
        }
        if(!visibility.get(3)){
            recruitment = null;
        }
        visibility = null;
        return this;
    }

    @Override
    public Group clone() {
        try {
            Group clone = (Group) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
