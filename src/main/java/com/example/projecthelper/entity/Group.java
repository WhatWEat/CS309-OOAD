package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@TableName("groups")
public class Group {
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
}
