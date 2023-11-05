package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.mapper.*;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssignmentService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private SubmittedAssMapper submittedAssMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private GroupMapper groupMapper;

    public void createAss(Assignment assignment, Long creatorId, Predicate<Long> accessProject){
        //创建作业，assignment中其实包含了creatorId的属性,这个creatorId由控制器获取token中的Id传进来
        if(!accessProject.test(assignment.getProjectId())){
            System.err.println(assignment.getProjectId());
            throw new AccessDeniedException("无权创建作业");
        }
        if(!"i".equals(assignment.getType()) && !"g".equals(assignment.getType()))
            throw new InvalidFormException("发布作业类型无效");
        try {

            assignment.setCreatorId(creatorId);
            assignmentMapper.createAss(assignment);
        } catch (PSQLException e) {
            System.err.println(e.getMessage());
            throw new InvalidFormException("title, description不能为空");
        }
    }

    public void submitAss(SubmittedAssignment submittedAss, Long userId){
        //FUNC: 检查学生是否有权限提交作业, 即是否在proj中
        Assignment originAss = assignmentMapper.findAssById(submittedAss.getAssignmentId());
        if(originAss == null || projectMapper.checkStuInProj(userId, originAss.getProjectId()) == null){
            throw new AccessDeniedException("无权提交作业");
        }
        //PROC: 先判断这个是group assignment还是individual assignment
        //NOTE: submittedAss的assignmentId是对应的作业ID
        if(originAss.getType().equals("i")){
            // 个人提交
            submittedAss.setSubmitterId(userId);
            submittedAss.setSubmitTime(LocalDateTime.now());
            try{
                // FUNC: 对原本的作业进行一个覆盖
                submittedAssMapper.deleteOriginalSubmit(submittedAss);
                submittedAssMapper.submitAss(submittedAss);
            }
            catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
        else {
            // 个人为集体提交
            Long gpId = groupMapper.findGroupIdOfUserInAProj(userId, originAss.getProjectId());
            if(gpId == null)
                throw new AccessDeniedException("无权提交小组作业");
            submittedAss.setSubmitterId(gpId);
            try{
                // FUNC: 对原本的作业进行一个覆盖
                submittedAssMapper.deleteOriginalSubmit(submittedAss);
                submittedAssMapper.submitAss(submittedAss);
            }
            catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }

    public void removeAss(Long submitId, Long userId){
        SubmittedAssignment submittedAssignment = submittedAssMapper.findSubmittedAssignmentById(submitId);
        if(submittedAssignment == null)
            throw new AccessDeniedException("无权删除作业");
        Assignment assignment = assignmentMapper.findAssById(submittedAssignment.getAssignmentId());
        if(assignment.getType().equals("i") &&
            Objects.equals(submittedAssignment.getSubmitId(), userId))
            submittedAssMapper.removeAss(submitId);
        else if (assignment.getType().equals("g") &&
            Objects.equals(groupMapper.findGroupIdOfUserInAProj(userId, assignment.getProjectId()),
                submittedAssignment.getSubmitterId()))
            submittedAssMapper.removeAss(submitId);
        throw new AccessDeniedException("无权删除作业");
    }

    public SubmittedAssignment viewSubByStu(long submitId, long stuId){
//        System.err.println(submitId);
        SubmittedAssignment submittedAssignment = submittedAssMapper.findSubmittedAssignmentById(submitId);
//        System.err.println(submittedAssignment);
        if(submittedAssignment == null)
            throw new AccessDeniedException("无权查看作业");
        Assignment assignment = assignmentMapper.findAssById(submittedAssignment.getAssignmentId());
        if(assignment.getType().equals("i") &&
            Objects.equals(submittedAssignment.getSubmitId(), stuId))
            return submittedAssMapper.viewSub(submitId);
        else if (assignment.getType().equals("g") &&
            Objects.equals(groupMapper.findGroupIdOfUserInAProj(stuId, assignment.getProjectId()),
                submittedAssignment.getSubmitterId()))
            return submittedAssMapper.viewSub(submitId);
        throw new AccessDeniedException("无权查看作业");
    }

    public List<SubmittedAssignment> viewAllSub(long assignmentId, long teaId){
        Assignment assignment = assignmentMapper.findAssById(assignmentId);
        if (assignment == null){
            throw new InvalidFormException("作业不存在");
        }
        if (projectMapper.findTeacherByProject(assignment.getProjectId()) != teaId){
            throw new AccessDeniedException("无权查看别人发布的作业");
        }
        return submittedAssMapper.findAllSub(assignmentId);
    }

    public void gradeAss(SubmittedAssignment submittedAssignment, Long teaId){
        SubmittedAssignment sub = submittedAssMapper.viewSub(submittedAssignment.getSubmitId());
        if(sub == null){
            throw new InvalidFormException("作业不存在");
        }
        Assignment assignment = assignmentMapper.findAssById(sub.getAssignmentId());
        if (!Objects.equals(projectMapper.findTeacherByProject(assignment.getProjectId()), teaId)){
            throw new AccessDeniedException("无权批改别人发布的作业");
        }
        try{
            submittedAssMapper.gradeAss(submittedAssignment);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }

    }

//    public boolean ifStuSub ( long assignmentId, long stuId){
//        return submittedAssMapper.findStuSubByAss(assignmentId, stuId) != null;
//    }
//
//    public boolean ifGroupSub ( long assignmentId, long groupId){
//        return submittedAssMapper.findGroupSubByAss(assignmentId, groupId) != null;
//    }
//
//    public boolean stuSubmitAss(long assignmentId,long stuId, String text, String filepath){
//        Assignment assignment = assignmentMapper.findAssById(assignmentId);
//        long projectId = assignment.getProjectId();
//        List<Long> projects = usersMapper.findProByStu(stuId);
//        if (projects.contains(projectId)){
//            SubmittedAssignment sub = new SubmittedAssignment(assignmentId,projectId,text,filepath);
//            try {
//                submittedAssMapper.submitAss(sub);
//            } catch (PSQLException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                submittedAssMapper.stuSubmitAss(sub.getSubmitId(),stuId);
//            } catch (PSQLException e) {
//                throw new RuntimeException(e);
//            }
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//    public boolean groupSubmitAss(long assignmentId,long groupId, String text, String filepath){
//        Assignment assignment = assignmentMapper.findAssById(assignmentId);
//        long projectId = assignment.getProjectId();
//        Group group = groupMapper.findGroupById(groupId);
//
//        if (group.getProjectId() == projectId){
//            SubmittedAssignment sub = new SubmittedAssignment(assignmentId,projectId,text,filepath);
//            try {
//                submittedAssMapper.submitAss(sub);
//            } catch (PSQLException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                submittedAssMapper.groupSubmitAss(sub.getSubmitId(),groupId);
//            } catch (PSQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            return true;
//        }else {
//            return false;
//        }
//    }



    //这里有bug，如果身份验证失败返回空list，实际应该有更严谨的失败返回




    //这里有bug，如果身份验证失败返回空sub，实际应该有更严谨的失败返回

//    public SubmittedAssignment viewSub(long teaId, long submitId){
//        SubmittedAssignment sub = submittedAssMapper.viewSub(submitId);
//        long projectId = sub.getProjectId();
//        long creatorId = projectMapper.findTeacherByProject(projectId);
//        if (teaId == creatorId){
//            return sub;
//        }else {
//            SubmittedAssignment submittedAssignment = new SubmittedAssignment();
//            return submittedAssignment;
//        }
//
//    }

    //这里有bug，如果身份验证失败返回空list，实际应该有更严谨的失败返回

    public List<SubmittedAssignment> stuSub (long projectId, long stuId){
        List<Long> projects = usersMapper.findProByStu(stuId);
        if (projects.contains(projectId)){
            return submittedAssMapper.findStuSubByProject(projectId,stuId);
        }else {
            List<SubmittedAssignment> list = new ArrayList<>();
            return list;
        }
    }

    public List<SubmittedAssignment> groupSub (long projectId, long groupId){
        Group group = groupMapper.findGroupById(groupId);

        if (projectId == group.getProjectId()){
            return submittedAssMapper.findGroupSubByProject(projectId,groupId);
        }else {
            List<SubmittedAssignment> list = new ArrayList<>();
            return list;
        }
    }



}
