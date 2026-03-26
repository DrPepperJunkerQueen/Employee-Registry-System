package com.example.rest;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(0)
    @Max(6)
    @Column(nullable = false)
    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ClassEmployee group;

    @Column(nullable = false)
    private LocalDate date;

    private String comment;

    @Transient
    private Integer groupId;

    public Rate() {
    }

    public Rate(int grade, ClassEmployee group, LocalDate date) {
        this(grade, group, date, null);
    }

    public Rate(int grade, ClassEmployee group, LocalDate date, String comment) {
        this.grade = grade;
        this.group = group;
        this.date = date;
        this.comment = comment;
    }

    // Gettery i settery

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public ClassEmployee getGroup() {
        return group;
    }

    public void setGroup(ClassEmployee group) {
        this.group = group;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Pomocnicza metoda - może zostać usunięta, jeśli niepotrzebna w REST
    public String getGroupName() {
        return this.group != null ? this.group.getEmployeeGroupName() : null;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
