package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import com.example.projecthelper.mapper.UsersMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private NoticeMapper noticeMapper;


    private final Logger logger = LoggerFactory.getLogger(GroupService.class);

    public void createProject(Project project){
        try {
            projectMapper.createProject(project);
        } catch (PSQLException e) {
            throw new InvalidFormException("The name of the project must not be null");
        }
    }

    public void addStuToProject(Long ProjectId, List<Long> userIds, Long currentUserId){
        // 先验证身份
        Long projCreatorId = projectMapper.findTeacherByProject(ProjectId);
        if(!Objects.equals(projCreatorId, currentUserId))
            throw new AccessDeniedException("您没有权限修改该公告");

        List<User> users = usersMapper.findUsersById(userIds);
        Set<Long> idSet = users.stream().filter(e -> e.getIdentity() == 3).map(User::getUserId).collect(Collectors.toSet());
        idSet.removeAll(new HashSet<>(projectMapper.findStuIdsByProject(projCreatorId)));
        projectMapper.insertStuIds(projCreatorId, idSet);
    }

    public Long findTeacherByProject(Long projectId){
        return projectMapper.findTeacherByProject(projectId);
    }

    public void stuInProject(long projectId, long stuId){
        usersMapper.stuInProject(projectId,stuId);
    }

    public List<String> getIntendedTeammates(long projectId, Long stuId){
        return projectMapper.getIntendedTeammates(projectId, stuId);
    }

    public void setIntendedTeammates(long projectId, Long stuId, List<String> intendedTeammates){
        projectMapper.setIntendedTeammates(projectId, stuId, intendedTeammates);
    }
}
