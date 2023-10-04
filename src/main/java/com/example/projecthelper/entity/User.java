package com.example.projecthelper.entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long user_id;

    private final String identity;

    private String password;

    private String phone;

    private String mail;

    private final String name;

    private final String gender;

    private String birthday;

    private String technology_stack;

    private String programming_skills;

    private String intended_teammates;

    public User( String identity, String password, String name, String gender) {

        this.identity = identity;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }
}

