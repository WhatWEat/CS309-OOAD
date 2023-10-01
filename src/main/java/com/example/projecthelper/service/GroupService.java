package com.example.projecthelper.service;

import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    //TODO:创建group
    public void createGroup(Group group){
        String newId = UUID.randomUUID().toString();
    }

    //TODO:修改group的信息
    public void modifyGroupInfo(Group group){

    }

    //TODO:学生加入group
    public void joinGroup(Group group, String jwt){

    }
}
