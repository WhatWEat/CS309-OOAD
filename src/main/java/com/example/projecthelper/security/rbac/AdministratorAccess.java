package com.example.projecthelper.security.rbac;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorAccess implements Access {

    private final NoticeMapper noticeMapper;
    private final ProjectMapper projectMapper;

    @Autowired
    public AdministratorAccess(NoticeMapper noticeMapper, ProjectMapper projectMapper) {
        this.noticeMapper = noticeMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public boolean accessNotice(Long userId, Notice notice) {
        return notice != null && notice.getType() == 0;
    }

    @Override
    public boolean accessProject(Long userId, Project project) {
        return project != null;
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
