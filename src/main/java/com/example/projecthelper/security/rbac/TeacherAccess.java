package com.example.projecthelper.security.rbac;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.Notice;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

@Service
public class TeacherAccess implements Access {

    private final NoticeMapper noticeMapper;
    private final ProjectMapper projectMapper;

    @Autowired
    public TeacherAccess(NoticeMapper noticeMapper, ProjectMapper projectMapper) {
        this.noticeMapper = noticeMapper;
        this.projectMapper = projectMapper;
    }

    public boolean accessNotice(Long teaId, Notice notice){
        return Objects.equals(
            teaId, projectMapper.findTeacherByProject(notice.getProjectId())
        ) && notice.getType() == 0;
    }

    @Override
    public boolean accessProject(Long userId, Project project) {
        return Objects.equals(project.getTeacherId(), userId);
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
