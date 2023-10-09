package com.example.projecthelper.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {
    private Integer user_id;

    private final int identity;

    private String password;

    private String phone;

    private String mail;

    private final String name;

    private final String gender;

    private Date birthday;

    private String technology_stack;

    private String programming_skills;

    private String intended_teammates;

    public User( int identity, String password, String name, String gender) {
        this.identity = identity;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }
}

