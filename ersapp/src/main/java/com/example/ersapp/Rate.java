package com.example.piec;

import jakarta.persistence.*;

import java.time.LocalDate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@Entity
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(0)
    @Max(6)
    private int grade;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private ClassEmployee group;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String comment;

    public Rate()
    {

    }

    public Rate(int grade, ClassEmployee group, LocalDate date) {
        this(grade, group, date, null);
    }

    public Rate(int grade, ClassEmployee group, LocalDate date, String comment)
    {
        this.grade = grade;
        this.group = group;
        this.date = date;
        this.comment = comment;
    }

    public void setId(int id) {this.id = id;}
    public int getId() {return id;}
    public void setGrade(int grade) {this.grade = grade;}
    public int getGrade() {return grade;}
    public void setGroup(ClassEmployee group) {this.group = group;}
    public ClassEmployee getGroup() {return group;}
    public void setDate(LocalDate date) {this.date = date;}
    public LocalDate getDate() {return date;}
    public void setComment(String comment) {this.comment = comment;}
    public String getComment() {return comment;}
    public String getGroupName() {return this.group.getEmployeeGroupName();}
}

