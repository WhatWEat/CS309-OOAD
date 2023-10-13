package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    @TableField("user_id")
    private Integer userId;

    private int identity;

    private String password;

    private String phone;

    private String mail;

    private String name;

    private String gender;

    private Date birthday;
    @TableField("technology_stack")
    private String technologyStack;
    @TableField("programming_skills")
    private String programmingSkills;
    @TableField("intended_teammates")
    private String intendedTeammates;

    public User( int identity, String password, String name, String gender) {
        this.identity = identity;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    public User() {
    }
}

