package com.example.projecthelper.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notice {
    private final Integer notice_id;

    private String title;
    private String content;
    private final Integer creator_id;

    public Notice(Integer notice_id, String title, String content, Integer creator_id) {
        this.notice_id = notice_id;
        this.title = title;
        this.content = content;
        this.creator_id = creator_id;
    }

}
