package com.example.rest;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RateDTO {
    private int id;
    private int grade;
    @JsonProperty("groupId")
    private Long groupId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String comment;

    // Gettery i settery

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
