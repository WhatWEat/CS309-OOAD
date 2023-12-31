package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.Exceptions.OverdueException;
import com.example.projecthelper.cache.AssignmentCache;
import com.example.projecthelper.entity.*;
import com.example.projecthelper.mapper.*;
import com.example.projecthelper.util.FileUtil;
import com.example.projecthelper.util.Wrappers.KeyValueWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

@Service
public class AssignmentService {
    private final ProjectMapper projectMapper;
    private final AssignmentMapper assignmentMapper;
    private final SubmittedAssMapper submittedAssMapper;
    private final UsersMapper usersMapper;
    private final GroupMapper groupMapper;
    private final FileService fileService;
    private final AssignmentCache assignmentCache;

    @Autowired
    public AssignmentService(ProjectMapper projectMapper, AssignmentMapper assignmentMapper,
                             SubmittedAssMapper submittedAssMapper, UsersMapper usersMapper,
                             GroupMapper groupMapper, FileService fileService,
                             AssignmentCache assignmentCache) {
        this.projectMapper = projectMapper;
        this.assignmentMapper = assignmentMapper;
        this.submittedAssMapper = submittedAssMapper;
        this.usersMapper = usersMapper;
        this.groupMapper = groupMapper;
        this.fileService = fileService;
        this.assignmentCache = assignmentCache;
    }

    public List<Assignment> getAssignmentsByAdm(int page, int pageSize) {
        List<Assignment> results = assignmentMapper.getAssByAdm(pageSize, page * pageSize);

        results.forEach(a ->
                a.setFilePaths(a.getFilePaths().stream().map(FileUtil::getFilenameFromPath).toList())
        );
        return results;
    }


    public List<Assignment> getAssignmentsByTea(Long userId, Long projId, int page, int pageSize) {
        List<Assignment> results;
        if (projId == -1) {
            results = assignmentMapper.getAssByTea(userId, pageSize, page * pageSize);
        } else {
            Long teaOfProj = projectMapper.findTeacherByProject(projId);
            if (!Objects.equals(teaOfProj, userId)) {
                throw new AccessDeniedException("无权访问该project");
            }
            results = assignmentCache.getAssignmentsInProj(projId, pageSize, page * pageSize);
        }
        results.forEach(a -> {
                    if (a.getFilePaths() != null)
                        a.setFilePaths(a.getFilePaths().stream().map(FileUtil::getFilenameFromPath).toList());
            if (a.getAssignmentId() == 34) {
                System.err.println(a.getFilePaths());
            }
                }
        );
        return results;
    }

    public List<Assignment> getAssignmentsByTa(Long userId, Long projId, int page, int pageSize) {
        List<Assignment> results;
        if (projId == -1) {
            results = assignmentMapper.getAssByTa(userId, pageSize, page * pageSize);
        } else {
            Long taOfProj = projectMapper.checkTaInProj(projId, userId);
            if (Objects.equals(taOfProj, null)) {
                throw new AccessDeniedException("无权访问该project");
            }

            results =
                    assignmentCache.getAssignmentsInProj(projId, pageSize, page * pageSize);
        }
        results.forEach(a ->
                a.setFilePaths(a.getFilePaths().stream().map(FileUtil::getFilenameFromPath).toList())
        );
        return results;
    }

    public List<Assignment> getAssignmentsByStu(Long userId, Long projId, int page, int pageSize) {
        List<Assignment> results;
        if (projId == -1) {
            results = assignmentMapper.getAssByStu(userId, pageSize, page * pageSize);
        } else {
            Long checker = projectMapper.checkStuInProj(userId, projId);
            if (Objects.equals(checker, null)) {
                throw new AccessDeniedException("无权访问该project");
            }


            results = assignmentMapper.getAssByProj(projId, pageSize, page * pageSize);
        }
        try {
            results.forEach(a ->
                    a.setFilePaths(a.getFilePaths().stream().map(FileUtil::getFilenameFromPath).toList())
            );
        } catch (NullPointerException ignored) {

        }
        return results;
    }

    public KeyValueWrapper<Assignment, SubmittedAssignment> getAssById(Long ass_id, Long user_id, int identity){
        Assignment assignment = assignmentMapper.findAssById(ass_id);
        if(assignment == null){
            throw new InvalidFormException("assId不正确");
        }
        switch (identity){
            case 1:
                if(!Objects.equals(user_id, projectMapper.findTeacherByProject(assignment.getProjectId())))
                    throw new AccessDeniedException("无权访问该ass");
                break;
            case 2:
                if(!Objects.equals(user_id, projectMapper.checkTaInProj(assignment.getProjectId(), user_id)))
                    throw new AccessDeniedException("无权访问该ass");
                break;
            case 3:
                if(!Objects.equals(user_id, projectMapper.checkStuInProj(user_id, assignment.getProjectId())))
                    throw new AccessDeniedException("无权访问该ass");
                break;
        }
        String type = assignment.getType();
        SubmittedAssignment submittedAssignment = null;
        if(identity == 3){
            if(type.equals("i")){
                submittedAssignment = assignmentMapper.findSubAssById(ass_id, user_id);
                if(submittedAssignment != null){
                    User user = usersMapper.findUserById(user_id);
                    submittedAssignment.setSubmitterName(user.getName());
                }
            }
            if(type.equals("g")){
                Long gpId = groupMapper.findGroupIdOfUserInAProj(user_id, assignment.getProjectId());
                submittedAssignment = assignmentMapper.findSubAssById(ass_id, gpId);
                if(submittedAssignment != null){
                    Group gp = groupMapper.findGroupById(gpId);
                    submittedAssignment.setSubmitterName(gp.getGroupName());
                }
            }
        }
        else {
            submittedAssignment = assignmentMapper.findLatestSubAssByAssId(ass_id);
            if(submittedAssignment != null){
                if(type.equals("i")){
                    User user = usersMapper.findUserById(submittedAssignment.getSubmitterId());
                    submittedAssignment.setSubmitterName(user.getName());
                }
                else if (type.equals("g")){
                    Group gp = groupMapper.findGroupById(submittedAssignment.getSubmitterId());
                    submittedAssignment.setSubmitterName(gp.getGroupName());
                }
            }
        }
        assignment.setState(
            Assignment.AssignmentState.getState(assignment, submittedAssignment).getValue()
        );
        try{
            assignment.setFilePaths(
                assignment.getFilePaths().stream().map(FileUtil::getFilenameFromPath).toList()
            );
        }catch (NullPointerException ignored){

        }
        try {
            submittedAssignment.setFilepaths(
                submittedAssignment.getFilepaths().stream().map(FileUtil::getFilenameFromPath).toList()
            );
        }catch (NullPointerException ignored){

        }
        return new KeyValueWrapper<>(assignment, submittedAssignment);
    }

    public void createAss(Assignment assignment, Long creatorId, Predicate<Long> accessProject) {
        //创建作业，assignment中其实包含了creatorId的属性,这个creatorId由控制器获取token中的Id传进来
        if (!accessProject.test(assignment.getProjectId())) {
            System.err.println(assignment.getProjectId());
            throw new AccessDeniedException("无权创建作业");
        }
        if (!"i".equals(assignment.getType()) && !"g".equals(assignment.getType()) && !"e".equals(assignment.getType()))
            throw new InvalidFormException("发布作业类型无效");
        if (!assignment.getDeadline().isAfter(LocalDateTime.now()))
            throw new InvalidFormException("ddl必须晚于现在的时间");

        assignment.setReleaseTime(LocalDateTime.now());
        assignment.setCreatorId(creatorId);
        assignmentCache.createAss(assignment);
        System.err.println(assignment.getAssignmentId());
        // FUNC: 创建文件目录
        if (assignment.getFiles() != null && !assignment.getFiles().isEmpty()) {
            assignment.setFilePaths(new ArrayList<>());
            for (MultipartFile file : assignment.getFiles()) {

                String pth = FileUtil.generateAssPath(assignment);
                String fp = FileUtil.saveFile(pth, file.getOriginalFilename(), file);
                assignment.getFilePaths().add(fp);
            }
            assignmentMapper.updateFilePathOfAss(assignment.getFilePaths(), assignment.getAssignmentId());
        }
    }

    public void deleteAss(Long assignmentId, Long userId, Integer identity) {
        Assignment ass = assignmentMapper.findAssById(assignmentId);
        if (ass == null)
            throw new AccessDeniedException("无效的作业id");
        if (identity == 1) {
            Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
            if (!Objects.equals(teaId, userId))
                throw new AccessDeniedException("无权查看别人发布的作业");
        } else {
            Long taId = projectMapper.checkTaInProj(ass.getProjectId(), userId);
            if (taId == null)
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        assignmentCache.deleteAss(ass.getProjectId(), assignmentId);
        submittedAssMapper.deleteSubmittedAssByAssId(assignmentId);
    }

    public void updateAss(Assignment assignment, Long userId) {
        Assignment originAss = assignmentMapper.findAssById(assignment.getAssignmentId());
    }

    public void submitAss(SubmittedAssignment submittedAss, Long userId) {
        //FUNC: 检查学生是否有权限提交作业, 即是否在proj中
        Assignment originAss = assignmentMapper.findAssById(submittedAss.getAssignmentId());
        if (originAss == null || projectMapper.checkStuInProj(userId, originAss.getProjectId()) == null) {
            System.err.println(originAss);
            System.err.println(userId);
            throw new AccessDeniedException("无权提交作业");
        }
        //PROC: 先判断这个是group assignment还是individual assignment
        //NOTE: submittedAss的assignmentId是对应的作业ID
        if (LocalDateTime.now().isAfter(originAss.getDeadline())) {
            throw new OverdueException("超时未完成", originAss.getDeadline(), LocalDateTime.now());
        }
        if (originAss.getType().equals("i")) {
            // 个人提交
            submittedAss.setSubmitterId(userId);
            submittedAss.setSubmittedTime(LocalDateTime.now());
            try {
                // FUNC: 对原本的作业进行一个覆盖
                fileService.removeFilesOfSubmittedAss(originAss, submittedAss.getSubmitterId());
                submittedAssMapper.deleteOriginalSubmit(submittedAss);
                if (submittedAss.getFiles() != null && !submittedAss.getFiles().isEmpty()) {
                    //FUNC: 先检查拓展名
                    for (MultipartFile file : submittedAss.getFiles()) {
                        if (!FileUtil.hasExtension(file, originAss.getRequireExtension())) {
                            throw new InvalidFormException("请按作业要求提交文件");
                        }
                    }
                    String path = FileUtil.generateSubmittedAssPath(originAss, submittedAss.getSubmitterId());
                    List<String> fps = new ArrayList<>();
                    for (MultipartFile file : submittedAss.getFiles()) {
                        String fp = FileUtil.saveFile(path, file.getOriginalFilename(), file);
                        fps.add(fp);
                    }
                    submittedAss.setFilepaths(fps);
                }
                submittedAssMapper.submitAss(submittedAss);
            } catch (InvalidFormException ife) {
                throw new InvalidFormException(ife.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            // 个人为集体提交
            Long gpId = groupMapper.findGroupIdOfUserInAProj(userId, originAss.getProjectId());
            if (gpId == null)
                throw new AccessDeniedException("无权提交小组作业");
            submittedAss.setSubmitterId(gpId);
            try {
                // FUNC: 对原本的作业进行一个覆盖
                fileService.removeFilesOfSubmittedAss(originAss, submittedAss.getSubmitterId());
                submittedAssMapper.deleteOriginalSubmit(submittedAss);
                if (submittedAss.getFiles() != null && !submittedAss.getFiles().isEmpty()) {
                    //FUNC: 先检查拓展名
                    for (MultipartFile file : submittedAss.getFiles()) {
                        if (!FileUtil.hasExtension(file, originAss.getRequireExtension())) {
                            throw new InvalidFormException("请按作业要求提交文件");
                        }
                    }
                    String path = FileUtil.generateSubmittedAssPath(originAss, submittedAss.getSubmitterId());
                    List<String> fps = new ArrayList<>();
                    for (MultipartFile file : submittedAss.getFiles()) {
                        String fp = FileUtil.saveFile(path, file.getOriginalFilename(), file);
                        fps.add(fp);
                    }
                    submittedAss.setFilepaths(fps);
                }
                submittedAssMapper.submitAss(submittedAss);
            } catch (InvalidFormException ife) {
                throw new InvalidFormException(ife.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public void submitEva(Evaluation evaluation, Long userId) {
        //FUNC: 检查学生是否有权限提交作业, 即是否在proj中
        Assignment originAss = assignmentMapper.findAssById(evaluation.getAssignmentId());
        if (originAss == null || projectMapper.checkStuInProj(userId, originAss.getProjectId()) == null) {
            throw new AccessDeniedException("无权提交作业");
        }
        //PROC: 先判断这个是group assignment还是individual assignment
        //NOTE: submittedAss的assignmentId是对应的作业ID
        if (LocalDateTime.now().isAfter(originAss.getDeadline())) {
            throw new OverdueException("超时未完成", originAss.getDeadline(), LocalDateTime.now());
        }
        //小组互评
        Long gpId = groupMapper.findGroupIdOfUserInAProj(userId, originAss.getProjectId());
        if (gpId == null) {
            throw new AccessDeniedException("无权提交小组作业");
        }
        Group group = groupMapper.findGroupById(gpId);
        evaluation.setCommentGroup(gpId);
        if (!userId.equals(group.getLeaderId())) {
            throw new AccessDeniedException("您不是组长");
        }

//        try {// FUNC: 对原本的作业进行一个覆盖
            submittedAssMapper.deleteOriginalEva(evaluation);
            submittedAssMapper.submitEva(evaluation);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
    }

    public void removeSubmittedAss(Long assignmentId, Long userId) {
        Assignment assignment = assignmentMapper.findAssById(assignmentId);
        switch (assignment.getType()) {
            case "i" -> {
                fileService.removeFilesOfSubmittedAss(assignment, userId);
                submittedAssMapper.removeAss(assignmentId, userId);
            }
            case "g" -> {
                Long gpId = groupMapper.findGroupIdOfUserInAProj(userId, assignment.getProjectId());
                if (gpId != null) {
                    if (Objects.equals(userId, groupMapper.findLeaderByGroup(gpId))) {
                        fileService.removeFilesOfSubmittedAss(assignment, gpId);
                        submittedAssMapper.removeAss(assignmentId, gpId);
                        return;
                    }
                    else
                        throw new AccessDeniedException("你不是小组长");
                }
                throw new AccessDeniedException("你不在小组中");
            }
        }
    }


    public void removeEva(Long assignmentId, Long userId, Long commentedGroup) {
        Assignment assignment = assignmentMapper.findAssById(assignmentId);

        Long gpId = groupMapper.findGroupIdOfUserInAProj(userId, assignment.getProjectId());
        if (gpId != null) {
            Group group = groupMapper.findGroupById(gpId);
            if (!userId.equals(group.getLeaderId())) {
                throw new AccessDeniedException("您不是组长");
            }
            submittedAssMapper.removeEva(assignmentId, gpId, commentedGroup);
            return;
        }
        throw new AccessDeniedException("你不在小组中");
    }

    public SubmittedAssignment viewSubByStu(long assignmentId, long stuId) {
        Assignment assignment = assignmentMapper.findAssById(assignmentId);
        if (assignment.getType().equals("i")) {
            SubmittedAssignment sa = submittedAssMapper.viewSub(assignmentId, stuId);
            if (sa != null)
                sa.setFilepaths(sa.getFilepaths().stream().map(FileUtil::getFilenameFromPath).toList());
            return sa;
        } else if (assignment.getType().equals("g")) {
            Long gpId = groupMapper.findGroupIdOfUserInAProj(stuId, assignment.getProjectId());
            if (gpId != null) {
                SubmittedAssignment sa = submittedAssMapper.viewSub(assignmentId, gpId);
                if (sa != null)
                    sa.setFilepaths(sa.getFilepaths().stream().map(FileUtil::getFilenameFromPath).toList());
                return sa;
            }
            throw new AccessDeniedException("你不在小组中");
        }
        throw new AccessDeniedException("无权查看作业");
    }

    public Double viewEvaByStu(long assignmentId, long stuId) {
        Assignment assignment = assignmentMapper.findAssById(assignmentId);
        System.out.println(assignment.getProjectId());
        Long gpId = groupMapper.findGroupIdOfUserInAProj(stuId, assignment.getProjectId());

        System.out.println(gpId);
        if (gpId != null) {
            if (submittedAssMapper.avgGrade(gpId)!=null){
                return submittedAssMapper.avgGrade(gpId);
            }else return -1d;

        }
        throw new AccessDeniedException("无权查看作业");
    }

    public List<Long> selectToCommented(long assignmentId, long stuId) {
        Assignment assignment = assignmentMapper.findAssById(assignmentId);
        Long gpId = groupMapper.findGroupIdOfUserInAProj(stuId, assignment.getProjectId());
        if (gpId != null) {
            List<Evaluation> commented = submittedAssMapper.selectCommented(assignment.getProjectId(),gpId);
            List<Group> groups = groupMapper.findAllGroup(assignment.getProjectId());
            groups.removeIf(group -> group.getGroupId().equals(gpId));
            for (Evaluation commented1 : commented){
                groups.removeIf(group -> group.getGroupId().equals(commented1.getCommentedGroup()));
            }
            List<Long> out = new ArrayList<>();
            Random random = new Random();
            for ( int i = 0; i< 3-commented.size();i++){
                Group group = groups.get(random.nextInt(groups.size()));
                while (out.contains(group.getGroupId())){
                    group = groups.get(random.nextInt(groups.size()));
                }
                out.add(group.getGroupId());
            }
            return out;
        }
        throw new AccessDeniedException("无权查看作业");
    }

    public List<SubmittedAssignment> viewAllSub(long assignmentId, long userId, long page, long pageSize, Integer identity) {
        Assignment ass = assignmentMapper.findAssById(assignmentId);
        if (ass == null)
            throw new AccessDeniedException("无效的作业id");
        if (identity == 1) {
            Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
            if (!Objects.equals(teaId, userId))
                throw new AccessDeniedException("无权查看别人发布的作业");
        } else {
            Long taId = projectMapper.checkTaInProj(ass.getProjectId(), userId);
            if (taId == null)
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        List<SubmittedAssignment> sas = submittedAssMapper.findAllSub(assignmentId, pageSize, page * pageSize);
        if(ass.getType().equals("i")){
            sas.forEach(sa -> {
                sa.setSubmitterName(
                    usersMapper.findUserById(
                        sa.getSubmitterId()).getName()
                );
            });
        }
        else if(ass.getType().equals("g")){
            sas.forEach(sa -> {
                sa.setSubmitterName(
                    groupMapper.findGroupById(
                        sa.getSubmitterId()
                    ).getGroupName()
                );
            });
        }
        if (sas != null)
            try{
                sas.forEach(
                    sa -> sa.setFilepaths(sa.getFilepaths().stream().map(FileUtil::getFilenameFromPath).toList())
                );
            }catch (NullPointerException ignored){

            }
        return sas;
    }

    public List<Evaluation> viewEva(long assignmentId, long userId, float grade, long submitid, long togroup, Integer identity) {
        Assignment ass = assignmentMapper.findAssById(assignmentId);
        if (ass == null)
            throw new AccessDeniedException("无效的作业id");
        if (identity == 1) {
            Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
            if (!Objects.equals(teaId, userId))
                throw new AccessDeniedException("无权查看别人发布的作业");
        } else {
            Long taId = projectMapper.checkTaInProj(ass.getProjectId(), userId);
            if (taId == null)
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        List<Long> s = new ArrayList<>();
        List<Long> t = new ArrayList<>();
        List<Float> g = new ArrayList<>();
        if (submitid >= 0) {
            s.add(submitid);
        }
        if (togroup >= 0) {
            t.add(togroup);
        }
        if (grade >= 0) {
            g.add(grade);
        }
        return submittedAssMapper.searchEvaluation(s, t, g);
    }

    public void gradeAss(SubmittedAssignment submittedAssignment, Long userId, Integer identity) {
        if (submittedAssignment.getAssignmentId() == null){
            throw new InvalidFormException("学生尚未提交");
        }
        SubmittedAssignment sub = submittedAssMapper.viewSub(submittedAssignment.getAssignmentId(),
                submittedAssignment.getSubmitterId());
        if (sub == null) {
            throw new InvalidFormException("作业不存在");
        }
        Assignment ass = assignmentMapper.findAssById(sub.getAssignmentId());
        if (ass == null)
            throw new AccessDeniedException("无效的作业id");
        System.err.println(identity);
        if (identity == 1) {
            Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
            if (!Objects.equals(teaId, userId))
                throw new AccessDeniedException("无权查看别人发布的作业");
        } else {
            Long taId = projectMapper.checkTaInProj(ass.getProjectId(), userId);
            if (taId == null)
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        if (submittedAssignment.getGrade()>ass.getFullMark()){
            throw new AccessDeniedException("分数超出上限");
        }
        try {
            submittedAssMapper.gradeAss(submittedAssignment);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public List<SubmittedAssignment> getStuAllSub(Long projectId, Long userId,int page ,int pageSize){
        if(projectMapper.checkStuInProj(userId,projectId) == null){
            throw new AccessDeniedException("您不在project中");
        }
        List<SubmittedAssignment> submittedAssignments = allSub(projectId,userId);
        if (submittedAssignments.size()>=(page+1)*pageSize){
            return submittedAssignments.subList(page*pageSize,(page+1)*pageSize-1);
        }
        if (submittedAssignments.size()>=page*pageSize && submittedAssignments.size()<(page+1)*pageSize && submittedAssignments.size()>0){
            return submittedAssignments.subList(page*pageSize,submittedAssignments.size()-1);
        }
        else return new ArrayList<>();
    }
    public List<SubmittedAssignment> allSub(Long projectId, Long userId){
        Group group = groupMapper.findGroupOfStuInProject(userId, projectId);
        List<SubmittedAssignment> submittedAssignments = submittedAssMapper.findStuSubByProject(projectId,userId);
        User user = usersMapper.findUserById(userId);
        if (group!=null){
            submittedAssignments.addAll(submittedAssMapper.findGroupSubByProject(projectId,group.getGroupId()));
        }
        for (SubmittedAssignment submittedAssignment: submittedAssignments){
            submittedAssignment.setSubmitterName(user.getName());
            submittedAssignment.setTitle(assignmentMapper.findAssById(submittedAssignment.getAssignmentId()).getTitle());
        }
        return submittedAssignments;
    }
    public List<KeyValueWrapper<Long,List<SubmittedAssignment>>>getProAllSub(Long projectId, Predicate<Long> accessProject,
                                                                             int page, int pageSize){
        if (!accessProject.test(projectId)) {
            System.err.println(projectId);
            throw new AccessDeniedException("无权查看作业");
        }
        List<User> stus = usersMapper.findStuByProj(projectId);
        List<KeyValueWrapper<Long,List<SubmittedAssignment>>> submittedAssignments = new ArrayList<>();
        for (User stu : stus){
            submittedAssignments.add(new KeyValueWrapper<>(stu.getUserId(),allSub(projectId,stu.getUserId())));
        }
        if (submittedAssignments.size()>=(page+1)*pageSize){
            return submittedAssignments.subList(page*pageSize,(page+1)*pageSize-1);
        }
        if (submittedAssignments.size()>=page*pageSize && submittedAssignments.size()<(page+1)*pageSize && submittedAssignments.size()>0){
            return submittedAssignments.subList(page*pageSize,submittedAssignments.size()-1);
        }
        else return new ArrayList<>();
    }

    //这个方法是读取文件批量更新成绩
    public KeyValueWrapper<Assignment, List<SubmittedAssignment>> gradeAssWithFile(MultipartFile file, long assignmentId, Long userId, Integer identity) {
        Assignment ass = assignmentMapper.findAssById(assignmentId);
        if (ass == null)
            throw new AccessDeniedException("无效的作业id");
        if (identity == 1) {
            Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
            if (!Objects.equals(teaId, userId))
                throw new AccessDeniedException("无权查看别人发布的作业");
        } else {
            Long taId = projectMapper.checkTaInProj(ass.getProjectId(), userId);
            if (taId == null)
                throw new AccessDeniedException("无权查看别人发布的作业");
        }

        List<SubmittedAssignment> result = FileUtil.tableToSubmittedAssList(file);
//        result.forEach(System.err::println);
        submittedAssMapper.updateGrades(result, assignmentId);
        return new KeyValueWrapper<>(ass, result);
    }




    public int getAssState(long assId, long userId, Predicate<Long> accessProject) {
        Assignment assignment = assignmentMapper.findAssById(assId);
        if (assignment == null){
            throw new AccessDeniedException("无效的作业id");
        }
        Group group = groupMapper.findGroupOfStuInProject(userId, assignment.getProjectId());
        if (projectMapper.checkStuInProj(userId, assignment.getProjectId()) == null) {
            throw new AccessDeniedException("无权查看");
        }
        switch (assignment.getType()) {
            case "i" -> {
                SubmittedAssignment submittedAssignment = submittedAssMapper.findStuSubByAss(assId, userId);
                if (submittedAssignment == null) {
                    return 0;
                }
                if (submittedAssignment.getGrade() == null) {
                    return 1;
                }
                return 2;
            }
            case "g" -> {
                SubmittedAssignment submittedAssignment = submittedAssMapper.findGroupSubByAss(assId, group.getGroupId());
                if (submittedAssignment == null) {
                    return 0;
                }
                if (submittedAssignment.getGrade() == null) {
                    return 1;
                }
                return 2;
            }
            default -> {
                return -1;
            }
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

    public List<SubmittedAssignment> stuSub(long projectId, long stuId) {
        List<Long> projects = usersMapper.findProByStu(stuId);
        if (projects.contains(projectId)) {
            return submittedAssMapper.findStuSubByProject(projectId, stuId);
        } else {
            List<SubmittedAssignment> list = new ArrayList<>();
            return list;
        }
    }

    public List<SubmittedAssignment> groupSub(long projectId, long groupId) {
        Group group = groupMapper.findGroupById(groupId);

        if (projectId == group.getProjectId()) {
            return submittedAssMapper.findGroupSubByProject(projectId, groupId);
        } else {
            List<SubmittedAssignment> list = new ArrayList<>();
            return list;
        }
    }


}
