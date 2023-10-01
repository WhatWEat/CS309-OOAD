package com.example.projecthelper.entity;

import jakarta.persistence.criteria.CriteriaBuilder;

public class notice {
    private final Integer notice_id;

    private String title;
    private String content;
    private final Integer creator_id;

    public notice(Integer notice_id, String title, String content, Integer creator_id) {
        this.notice_id = notice_id;
        this.title = title;
        this.content = content;
        this.creator_id = creator_id;
    }

    public Integer getNotice_id() {
        return notice_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCreator_id() {
        return creator_id;
    }
}
