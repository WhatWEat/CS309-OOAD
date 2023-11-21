package com.example.projecthelper.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class User implements Cloneable{
    private Long userId;

    private Integer identity;

    private String password;

    private String phone;

    private String email;

    private String name;

    private String gender;

    private Date birthday;

    private List<String> programmingSkills;

    private String avatarPath;

    private boolean isFrozen;
    @TableField(exist = false)
    private MultipartFile avatar;
    @TableField(exist = false)
    private List<String> intendedTeammates;


    public User( Integer identity, String password, String name, String gender, Long userId) {
        this.identity = identity;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.userId = userId;
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

    public User mask(){
        password = null;
        avatarPath = null;
        return this;
    }
}

