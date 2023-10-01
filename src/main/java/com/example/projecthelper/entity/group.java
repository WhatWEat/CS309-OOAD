package com.example.projecthelper.entity;

public class group {
    private final Integer group_id;
    private Integer leader_id;

    private String group_name;
    private final Integer instructor_id;

    private Integer maxsize;

    public group(Integer group_id, Integer leader_id, String group_name, Integer instructor_id, Integer maxsize) {
        this.group_id = group_id;
        this.leader_id = leader_id;
        this.group_name = group_name;
        this.instructor_id = instructor_id;
        this.maxsize = maxsize;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public Integer getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(Integer leader_id) {
        this.leader_id = leader_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Integer getInstructor_id() {
        return instructor_id;
    }

    public Integer getMaxsize() {
        return maxsize;
    }

    public void setMaxsize(Integer maxsize) {
        this.maxsize = maxsize;
    }
}
