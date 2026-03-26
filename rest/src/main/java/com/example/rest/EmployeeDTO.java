package com.example.rest;

public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private EmployeeCondition condition;
    private int birthYear;
    private double salary;
    private Long groupId;

    // Gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public EmployeeCondition getCondition() { return condition; }
    public String getConditionToString() { return String.valueOf(condition); }
    public void setCondition(EmployeeCondition condition) { this.condition = condition; }

    public int getBirthYear() { return birthYear; }
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
}
