package com.example.projecthelper.service;

import com.example.projecthelper.entity.Project;
import com.example.projecthelper.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    public void createProject(String name){
        projectMapper.createProject(name);
    }
}
