package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Project;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.NoticeMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import java.util.ArrayList;
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

    public List<Project> getProjectList(KeyValueWrapper<Long, Integer> idIdentityWrapper, int page, int page_size){
        return switch (idIdentityWrapper.getValue()) {
            case 0 ->
                projectMapper.getProjByAdm(page_size, page * page_size);
            case 1 ->
                projectMapper.getProjByTea(idIdentityWrapper.getKey(), page_size, page * page_size);
            case 2 ->
                projectMapper.getProjByTa(idIdentityWrapper.getKey(), page_size, page * page_size);
            case 3 ->
                projectMapper.getProjByStu(idIdentityWrapper.getKey(), page_size, page * page_size);
            default -> null;
        };
    }

    public void createProject(Project project){
        try {
            projectMapper.createProject(project);
        } catch (PSQLException e) {
            throw new InvalidFormException("The name of the project must not be null");
        }
    }

    public void editProject(Project project, Long userId){
        if(!Objects.equals(userId, projectMapper.findTeacherByProject(project.getProjectId())))
            throw new InvalidFormException("无权修改小组");
        projectMapper.editProj(project);
    }
    public void addStuToProject(Long ProjectId, List<Long> userIds, Long currentUserId){
        // 先验证身份
        Long projCreatorId = projectMapper.findTeacherByProject(ProjectId);
        if(!Objects.equals(projCreatorId, currentUserId))
            throw new AccessDeniedException("您没有权限修改该公告");

        List<User> users = usersMapper.findUsersById(userIds);
        Set<Long> idSet = users.stream().filter(e -> e.getIdentity() == 3).map(User::getUserId).collect(Collectors.toSet());
        idSet.removeAll(new HashSet<>(projectMapper.findStuIdsByProject(projCreatorId)));
        if(!idSet.isEmpty())
            try {
                projectMapper.insertStuIds(ProjectId, idSet);
            }catch (Exception e){
                throw new InvalidFormException("请勿重复拉人进入项目");
            }
    }

    public Long findTeacherByProject(Long projectId){
        return projectMapper.findTeacherByProject(projectId);
    }

    public Long checkTaInProj(Long projectId, Long taId){
        return projectMapper.checkTaInProj(projectId, taId);
    }

    public void stuInProject(long projectId, long stuId){
        usersMapper.stuInProject(projectId,stuId);
    }

    public List<String> getIntendedTeammates(long projectId, Long stuId){
        List<String> result = projectMapper.getIntendedTeammates(projectId, stuId).getObject();
        System.err.println(result);
        return result;
    }

    public void addIntendedTeammates(long projectId, Long stuId, String intendedTeammate){
        List<String> intendedTeammates = projectMapper.getIntendedTeammates(projectId, stuId).getObject();
        if(intendedTeammates == null)
            throw new InvalidFormException("你不在proj中");
        // TODO: 进行异步处理
        System.err.println(intendedTeammates.getClass()+" "+intendedTeammate);
//        intendedTeammates = new ArrayList<>(intendedTeammates);
        intendedTeammates.add(intendedTeammate);
        projectMapper.setIntendedTeammates(projectId, stuId, intendedTeammates);
    }

    public void deleteIntendedTeammates(long projectId, Long stuId, String intendedTeammate){
        List<String> intendedTeammates = projectMapper.getIntendedTeammates(projectId, stuId).getObject();
        if(intendedTeammates == null)
            throw new InvalidFormException("你不在proj中");
        // TODO: 进行异步处理
        int index = intendedTeammates.indexOf(intendedTeammate);
        // 如果存在，则删除它
        if (index != -1) {
//            intendedTeammates = new ArrayList<>(intendedTeammates);
            intendedTeammates.remove(index);
        }
        projectMapper.setIntendedTeammates(projectId, stuId, intendedTeammates);

    }

    public void designateTaToProj(long projId, List<Long> taId, long userId){
        Long teaId = projectMapper.findTeacherByProject(projId);
        if(teaId != userId)
            throw new AccessDeniedException("无权修改别人的proj");
        if(taId == null)
            return;
        taId = taId.stream().filter(
            e -> {
                User ta = usersMapper.findUserById(e);
                Long id = projectMapper.checkTaInProj(projId, e);
                return ta != null && ta.getIdentity() == 2 && id == null;
            }
        ).distinct().toList();
        if(!taId.isEmpty())
            projectMapper.designateTaToProj(projId, taId);
    }

    public void removeTaFromProj(long projId, long taId, long userId){
        Long teaId = projectMapper.findTeacherByProject(projId);
        if(teaId != userId)
            throw new AccessDeniedException("无权修改别人的proj");
        projectMapper.removeTaFromProj(projId, taId);
    }
}
