package com.example.projecthelper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class SubmittedAssignment {
    private Long assignmentId;
    private Float grade;
    private Long submitterId;
    private String text;
    private String comment;
    private List<String> filepaths;
    private String review;
    private LocalDateTime submittedTime;
    private Long togroup;
    @TableField(exist = false)
    private List<MultipartFile> files;
    public SubmittedAssignment(Long submitId, Long assignmentId, Long submitterId) {
        this.assignmentId = assignmentId;
        this.submitterId = submitterId;
    }

    public SubmittedAssignment(Long submitterId, Float grade, String comment, String review){
        this.submitterId = submitterId;
        this.grade = grade;
        this.comment = comment;
        this.review = review;
    }


    public SubmittedAssignment() {
    }
}
