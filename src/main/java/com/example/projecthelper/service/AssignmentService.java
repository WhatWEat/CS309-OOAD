package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.Group;
import com.example.projecthelper.entity.SubmittedAssignment;
import com.example.projecthelper.mapper.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.stereotype.Service;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

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

    public void updateGrades (String filepath,long assignmentId){
        List<Long> submitterList = new ArrayList<>();
        List<Float> gradeList = new ArrayList<>();
        manageTableFile(filepath,submitterList,gradeList);

        submittedAssMapper.updateGrades(submitterList,gradeList,assignmentId);
    }

    //此方法从指定位置获取一个xls文件，并且读取第一张工作表,xlsx可能有问题，还在调试
    public static void manageTableFile(String filePath, List<Long> submitterList, List<Float>gradeList) {
        try {
            FileInputStream file = new FileInputStream(filePath);
//                FileInputStream file = new FileInputStream(new File(""));
            Workbook workbook;
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file);
            }else {
                System.out.println("Invalid file format. Only .xls and .xlsx files are supported.");
                return;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int idColumn = -1;
            int gradeColumn = -1;

            // 遍历第一行，查找“id”和“grade”列头所在的位置
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                String cellValue = cell.getStringCellValue().trim();
                if (cellValue.equalsIgnoreCase("id")) {
                    idColumn = i;
                } else if (cellValue.equalsIgnoreCase("grade")) {
                    gradeColumn = i;
                }
            }

            if (idColumn == -1 || gradeColumn == -1) {
                System.out.println("Name or grade column not found.");
                return;
            }

            // 存储数据的数组



            // 遍历所有行（除第一行外），将每一行的name和grade值存入相应的数组

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                long id = (long) dataRow.getCell(idColumn).getNumericCellValue();
                float grade =(float) dataRow.getCell(gradeColumn).getNumericCellValue();
                submitterList.add(id);
                gradeList.add( grade);
            }




//            System.out.println(Arrays.toString(names));
//            System.out.println(Arrays.toString(grades));

            file.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //这个方法用于检测文件的拓展名是否为指定的拓展名
    public static boolean hasExtension(File file, String extension) {
        String fileName = file.getName();
        return fileName.endsWith("." + extension);
    }

}
