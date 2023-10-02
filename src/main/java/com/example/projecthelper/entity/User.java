package com.example.projecthelper.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private final Integer user_id;

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

    public User(Integer user_id, String identity, String password, String name, String gender) {
        this.user_id = user_id;
        this.identity = identity;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    

//    public Integer getUser_id() {
//        return user_id;
//    }
//
//    public String getIdentity() {
//        return identity;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getMail() {
//        return mail;
//    }
//
//    public void setMail(String mail) {
//        this.mail = mail;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public String getBirthday() {
//        return birthday;
//    }
//
//    public String getTechnology_stack() {
//        return technology_stack;
//    }
//
//    public void setTechnology_stack(String technology_stack) {
//        this.technology_stack = technology_stack;
//    }
//
//    public String getProgramming_skills() {
//        return programming_skills;
//    }
//
//    public void setProgramming_skills(String programming_skills) {
//        this.programming_skills = programming_skills;
//    }
//
//    public String getIntended_teammates() {
//        return intended_teammates;
//    }
//
//    public void setIntended_teammates(String intended_teammates) {
//        this.intended_teammates = intended_teammates;
//    }
}

