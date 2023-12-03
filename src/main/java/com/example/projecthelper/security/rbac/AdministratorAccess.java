package com.example.projecthelper.security.rbac;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.Project;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class AdministratorAccess implements Access {


    @Override
    public boolean accessNotice(Long userId, Notice notice) {
        return false;
    }

    @Override
    public boolean accessProject(Long userId, Project project) {
        return false;
    }

    @Override
    public boolean accessGroup(Long userId, Group group) {
        return false;
    }

    @Override
    public boolean accessAssignment(Long userId, Assignment assignment) {
        return false;
    }
}
