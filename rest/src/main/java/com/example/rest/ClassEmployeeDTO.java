package com.example.rest;

public class ClassEmployeeDTO {
    private Long id;
    private String employeeGroupName;
    private int maxEmployeeNumber;

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeGroupName() {
        return employeeGroupName;
    }

    public void setEmployeeGroupName(String employeeGroupName) {
        this.employeeGroupName = employeeGroupName;
    }

    public int getMaxEmployeeNumber() {
        return maxEmployeeNumber;
    }

    public void setMaxEmployeeNumber(int maxEmployeeNumber) {
        this.maxEmployeeNumber = maxEmployeeNumber;
    }
}
