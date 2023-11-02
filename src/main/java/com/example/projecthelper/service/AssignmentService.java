package com.example.projecthelper.service;

import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.mapper.*;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean createAss(Assignment assignment, long teaId, long projectId){
        //创建作业，assignment中其实包含了creatorId的属性，这里teaId也许并不需要
        long creatorId = projectMapper.findTeacherByProject(projectId);
        if (creatorId == teaId){
            try {
                assignmentMapper.createAss(assignment);
                return true;
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            return false;
        }
    }

    public boolean ifStuSub ( long assignmentId, long stuId){
        return submittedAssMapper.findStuSubByAss(assignmentId, stuId) != null;
    }

    public boolean ifGroupSub ( long assignmentId, long groupId){
        return submittedAssMapper.findGroupSubByAss(assignmentId, groupId) != null;
    }

    public boolean stuSubmitAss(long assignmentId,long stuId, String text, String filepath){
        Assignment assignment = assignmentMapper.findAssById(assignmentId);
        long projectId = assignment.getProjectID();
        List<Long> projects = usersMapper.findProByStu(stuId);
        if (projects.contains(projectId)){
            SubmittedAssignment sub = new SubmittedAssignment(assignmentId,projectId,text,filepath);
            try {
                submittedAssMapper.submitAss(sub);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
            try {
                submittedAssMapper.stuSubmitAss(sub.getSubmitId(),stuId);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }else {
            return false;
        }
    }

    public boolean groupSubmitAss(long assignmentId,long groupId, String text, String filepath){
        Assignment assignment = assignmentMapper.findAssById(assignmentId);
        long projectId = assignment.getProjectID();
        Group group = groupMapper.findGroupById(groupId);

        if (group.getProjectId() == projectId){
            SubmittedAssignment sub = new SubmittedAssignment(assignmentId,projectId,text,filepath);
            try {
                submittedAssMapper.submitAss(sub);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
            try {
                submittedAssMapper.groupSubmitAss(sub.getSubmitId(),groupId);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }

            return true;
        }else {
            return false;
        }
    }

    public void stuRemoveAss(long submitId,long stuId){
        submittedAssMapper.removeStuAss(submitId,stuId);
        submittedAssMapper.removeAss(submitId);
    }

    public void groupRemoveAss(long submitId,long groupId){
        submittedAssMapper.removeGroupAss(submitId,groupId);
        submittedAssMapper.removeAss(submitId);

    }

    //这里有bug，如果身份验证失败返回空list，实际应该有更严谨的失败返回
    public List<SubmittedAssignment> viewAllSub(long teaId,long projectId, long assignmentId){
        long creatorId = projectMapper.findTeacherByProject(projectId);
        if (creatorId == teaId){
            return submittedAssMapper.findAllSub(projectId,assignmentId);
        }
        List<SubmittedAssignment> list = new ArrayList<>();
        return list;
    }



    //这里有bug，如果身份验证失败返回空sub，实际应该有更严谨的失败返回

    public SubmittedAssignment viewSub(long teaId, long submitId){
        SubmittedAssignment sub = submittedAssMapper.viewSub(submitId);
        long projectId = sub.getProjectId();
        long creatorId = projectMapper.findTeacherByProject(projectId);
        if (teaId == creatorId){
            return sub;
        }else {
            SubmittedAssignment submittedAssignment = new SubmittedAssignment();
            return submittedAssignment;
        }

    }

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

    public boolean gradeAss(long submitId, String comment, String review, float grade,long teaId){
        SubmittedAssignment sub = submittedAssMapper.viewSub(submitId);
        long projectId = sub.getProjectId();
        long creatorId = projectMapper.findTeacherByProject(projectId);
        if (teaId == creatorId){
            try {
                submittedAssMapper.gradeAss(grade,comment,submitId,review);
            } catch (PSQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

}
