package com.example.projecthelper.security.rbac;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.Project;

public interface Access {
    boolean accessNotice(Long userId, Notice notice);
    boolean accessProject(Long userId, Project project);
    boolean accessGroup(Long userId, Group group);
    boolean accessAssignment(Long userId, Assignment assignment);
}
