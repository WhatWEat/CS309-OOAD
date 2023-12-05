package com.example.projecthelper.service;

import com.example.projecthelper.Exceptions.FileProcessingException;
import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.User;
import com.example.projecthelper.mapper.AssignmentMapper;
import com.example.projecthelper.mapper.GroupMapper;
import com.example.projecthelper.mapper.ProjectMapper;
import com.example.projecthelper.mapper.UsersMapper;
import com.example.projecthelper.util.FileUtil;
import com.example.projecthelper.util.PDFConvert;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    private final AssignmentMapper assignmentMapper;
    private final ProjectMapper projectMapper;
    private final GroupMapper groupMapper;
    private final UsersMapper usersMapper;
    private final OssService ossService;

    @Autowired
    public FileService(AssignmentMapper assignmentMapper, ProjectMapper projectMapper, GroupMapper groupMapper,
                       UsersMapper usersMapper, OssService ossService) {
        this.assignmentMapper = assignmentMapper;
        this.projectMapper = projectMapper;
        this.groupMapper = groupMapper;
        this.usersMapper = usersMapper;
        this.ossService = ossService;
    }

    public Resource getAvatar(Long userId){
        User user = usersMapper.findUserById(userId);
        if(user == null)
            throw new InvalidFormException("找不到用户");
        try{
            Resource rec = new UrlResource(Paths.get(user.getAvatarPath()).normalize().toUri());
            if(rec.exists()){
                return rec;
            }
            else
                throw new FileProcessingException("找不到文件");
        }catch (MalformedURLException | FileProcessingException e){
            throw new FileProcessingException("找不到文件");
        }
    }

    public void removeOriAvatar(Long userId){
        User user = usersMapper.findUserById(userId);
        if(user.getAvatarPath() != null){
            ossService.deleteImage(ossService.toUrl(user.getAvatarPath(), userId));
//            Path pth = Paths.get(user.getAvatarPath());
//            try {
//                Files.delete(pth);
//            } catch (IOException e) {
//                // Handle the possible IOException here
//                System.err.println(e.getMessage());
//            }
        }

    }

    public Resource getFilesOfAssByTeaOrTa(Long userId, Long assId, String fileName, Integer identity, boolean isPdf){
        Assignment ass = assignmentMapper.findAssById(assId);
        if(ass == null)
            throw new AccessDeniedException("无效的作业id");
        if(identity == 1){
            Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
            if(!Objects.equals(teaId, userId))
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        else {
            Long taId = projectMapper.checkTaInProj(ass.getProjectId(),userId);
            if(taId == null)
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        Path fp = Paths.get(FileUtil.generateAssPath(ass)).resolve(fileName).normalize();
        System.err.println(fp.toString());
        try{
            if(!isPdf || fileName.endsWith(".pdf")){
                Resource rec = new UrlResource(fp.toUri());
                if(rec.exists()){
                    return rec;
                }
                else
                    throw new FileProcessingException("找不到文件");
            }
            else{
                Path op = Paths.get(FileUtil.generateAssPath(ass)).resolve(FileUtil.toPdfExtension(fileName)).normalize();
                PDFConvert.convertToPdf(fp.toString(), op.toString());
                Resource rec = new UrlResource(op.toUri());
                if(rec.exists()){
                    return rec;
                }
                else
                    throw new FileProcessingException("找不到文件");
            }
        }catch (MalformedURLException | FileProcessingException e){
            throw new FileProcessingException("找不到文件");
        }
    }

    public Resource getFilesOfAssByStu(Long userId, Long assId, String fileName, boolean isPdf){
        Assignment ass = assignmentMapper.findAssById(assId);
        if(ass == null)
            throw new AccessDeniedException("assignmentId不合法");
        if(projectMapper.checkStuInProj(userId, ass.getProjectId()) == null)
            throw new AccessDeniedException("你不在project中");
        Path fp = Paths.get(FileUtil.generateAssPath(ass)).resolve(fileName).normalize();
        System.err.println(fp.toString());
        try{
            if(!isPdf || fileName.endsWith(".pdf")) {
                Resource rec = new UrlResource(fp.toUri());
                if (rec.exists()) {
                    return rec;
                } else
                    throw new FileProcessingException("找不到文件");
            }
            else{
                Path op = Paths.get(FileUtil.generateAssPath(ass)).resolve(FileUtil.toPdfExtension(fileName)).normalize();
                PDFConvert.convertToPdf(fp.toString(), op.toString());
                Resource rec = new UrlResource(op.toUri());
                if(rec.exists()){
                    return rec;
                }
                else
                    throw new FileProcessingException("找不到文件");
            }
        }catch (MalformedURLException | FileProcessingException e){
            throw new FileProcessingException("找不到文件");
        }
    }

    public Resource getFilesOfSubmittedAss(Long userId, Long submitterId, Long assId, String fileName, Integer identity){
        Assignment ass = assignmentMapper.findAssById(assId);
        if(ass == null)
            throw new AccessDeniedException("无效的作业id");
        switch (identity){
            case 1: {
                Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
                if(!Objects.equals(teaId, userId))
                    throw new AccessDeniedException("无权查看别人发布的作业");
                break;
            }
            case 2:
                Long taId = projectMapper.checkTaInProj(ass.getProjectId(),userId);
                if(taId == null)
                    throw new AccessDeniedException("无权查看别人发布的作业");
                break;
            case 3:
                if(ass.getType().equals("i") && !Objects.equals(userId, submitterId) ||
                    ass.getType().equals("g") &&
                        !Objects.equals(groupMapper.findGroupIdOfUserInAProj(userId, ass.getProjectId()), submitterId)
                )
                    throw new AccessDeniedException("无权查看别人提交的作业");
                break;
        }
        Path fp = Paths.get(FileUtil.generateSubmittedAssPath(ass, submitterId)).resolve(fileName).normalize();
        try{
            Resource rec = new UrlResource(fp.toUri());
            if(rec.exists()){
                return rec;
            }
            else
                throw new FileProcessingException("找不到文件");
        }catch (MalformedURLException | FileProcessingException e){
            throw new FileProcessingException("找不到文件");
        }
    }
    public Resource getFilesOfSubmittedAssByTeaOrTa(Long userId, Long stuId, Long assId, String fileName, Integer identity){
        Assignment ass = assignmentMapper.findAssById(assId);
        if(ass == null)
            throw new AccessDeniedException("无效的作业id");
        if(identity == 1){
            Long teaId = projectMapper.findTeacherByProject(ass.getProjectId());
            if(!Objects.equals(teaId, userId))
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        else {
            Long taId = projectMapper.checkTaInProj(ass.getProjectId(),userId);
            if(taId == null)
                throw new AccessDeniedException("无权查看别人发布的作业");
        }
        Long submitterId = null;
        if(ass.getType().equals("i")){
            submitterId = stuId;
        }
        else {
            submitterId = groupMapper.findGroupIdOfUserInAProj(stuId, ass.getProjectId());
        }
        Path fp = Paths.get(FileUtil.generateSubmittedAssPath(ass, submitterId)).resolve(fileName).normalize();
        try{
            Resource rec = new UrlResource(fp.toUri());
            if(rec.exists()){
                return rec;
            }
            else
                throw new FileProcessingException("找不到文件");
        }catch (MalformedURLException | FileProcessingException e){
            throw new FileProcessingException("找不到文件");
        }
    }

    public Resource getFilesOfSubmittedAssByStu(Long userId, Long assId, String fileName){
        Assignment ass = assignmentMapper.findAssById(assId);
        if(ass == null)
            throw new AccessDeniedException("assignmentId不合法");
        if(projectMapper.checkStuInProj(userId, ass.getProjectId()) == null)
            throw new AccessDeniedException("你不在project中");
        Long submitterId = null;
        if(ass.getType().equals("i")){
            submitterId = userId;
        }
        else {
            submitterId = groupMapper.findGroupIdOfUserInAProj(userId, ass.getProjectId());
        }
        Path fp = Paths.get(FileUtil.generateSubmittedAssPath(ass, submitterId)).resolve(fileName).normalize();
        try{
            Resource rec = new UrlResource(fp.toUri());
            if(rec.exists()){
                return rec;
            }
            else
                throw new FileProcessingException("找不到文件");
        }catch (MalformedURLException | FileProcessingException e){
            throw new FileProcessingException("找不到文件");
        }
    }

    public void removeFilesOfAss(Long userId, Long assId){
        Assignment ass = assignmentMapper.findAssById(assId);
        if(ass == null || !Objects.equals(ass.getCreatorId(), userId))
            throw new AccessDeniedException("无权查看别人发布的作业");
        Path fp = Paths.get(FileUtil.generateAssPath(ass));
        try (Stream<Path> walk = Files.walk(fp)) {
            // We sort the stream in reverse order.
            // This ensures that we delete the files first, then the directories.
            walk.sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        // Handle the possible IOException here
                        System.err.println(e.getMessage());
                    }
                });
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void removeFilesOfSubmittedAss(Assignment assignment, Long submitterId){
        Path fp = Paths.get(FileUtil.generateSubmittedAssPath(assignment, submitterId));

        try (Stream<Path> walk = Files.walk(fp)) {
            // We sort the stream in reverse order.
            // This ensures that we delete the files first, then the directories.
            walk.sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        // Handle the possible IOException here
                        System.err.println(e.getMessage());
                    }
                });
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void removeFilesOfSubmittedAss(Long assId, Long submitterId){
        Assignment ass = assignmentMapper.findAssById(assId);
        if(ass == null)
            throw new AccessDeniedException("不合法的作业Id");
        Path fp = Paths.get(FileUtil.generateSubmittedAssPath(ass, submitterId));
        try (Stream<Path> walk = Files.walk(fp)) {
            // We sort the stream in reverse order.
            // This ensures that we delete the files first, then the directories.
            walk.sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        // Handle the possible IOException here
                        System.err.println(e.getMessage());
                    }
                });
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
