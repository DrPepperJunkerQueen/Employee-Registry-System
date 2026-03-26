package com.example.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "employee_groups")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeGroupName;

    @Column(nullable = false)
    private int maxEmployeeNumber;

    @JsonProperty("employees")
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employeeList = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rate> rates;

    public ClassEmployee() {}

    public ClassEmployee(String employeeGroupName, int maxEmployeeNumber) {
        if (employeeGroupName == null || employeeGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name can't be blank.");
        }
        if (maxEmployeeNumber <= 0) {
            throw new IllegalArgumentException("Max employee must be positive.");
        }

        this.employeeGroupName = employeeGroupName;
        this.maxEmployeeNumber = maxEmployeeNumber;
    }

    // Getters and setters

    public Long getId() {
        return id;
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

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = (employeeList != null) ? employeeList : new ArrayList<>();
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = (rates != null) ? rates : new ArrayList<>();
    }

    public Object getFillPercentage() {
        if (maxEmployeeNumber <= 0) {
            return 0.0;
        }

        int employeeCount = employeeList.size();
        return ((double) employeeCount / maxEmployeeNumber) * 100.0;
    }

    @Override
    public String toString() {
        return "ClassEmployee{" +
                "name='" + employeeGroupName + '\'' +
                ", max=" + maxEmployeeNumber +
                ", employees=" + employeeList +
                '}';
    }

    public boolean addEmployee(Employee employee) {
        if (employee == null) {
            return false;
        }

        // Initialize list if needed
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }

        // Check if there's room for more employees
        if (employeeList.size() >= maxEmployeeNumber) {
            return false;
        }

        // Add the employee
        employeeList.add(employee);
        return true;
    }

    // Metody typu pomocniczego np. addEmployee / removeEmployee zostaną przeniesione do serwisu.
}
