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
public class User implements Cloneable{
    private Long userId;

    private Integer identity;

    private String password;

    private String phone;

    private String mail;

    private String name;

    private String gender;

    private Date birthday;

    private String technologyStack;
    private String programmingSkills;
    private String intendedTeammates;


    public User( int identity, String password, String name, String gender) {
        this.identity = identity;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    public User() {
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

