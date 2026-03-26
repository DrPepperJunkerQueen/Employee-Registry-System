package com.example.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.Year;
import java.util.Objects;

@Entity
@Table(name = "employees")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee implements Comparable<Employee> {

    private static final double PRECISION = 1e-2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeCondition condition;

    @Column(nullable = false)
    private int birthYear;

    @Column(nullable = false)
    private double salary;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ClassEmployee group;

    public Employee() {
        // Required by JPA
    }

    public Employee(String firstName, String lastName, EmployeeCondition condition, int birthYear, double salary) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name can't be blank.");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name can't be blank.");
        }
        if (birthYear > Year.now().getValue() - 18) {
            throw new IllegalArgumentException("The employee must be an adult.");
        }
        if (salary < -PRECISION) {
            throw new IllegalArgumentException("Salary can't be negative.");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.condition = condition;
        this.birthYear = birthYear;
        this.salary = salary;
    }

    // === Gettery i settery ===

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EmployeeCondition getCondition() {
        return condition;
    }

    public void setCondition(EmployeeCondition condition) {
        this.condition = condition;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public ClassEmployee getGroup() {
        return group;
    }

    public void setGroup(ClassEmployee group) {
        this.group = group;
    }

    // === Metody dodatkowe ===

    @Override
    public int compareTo(Employee employee) {
        return this.lastName.compareTo(employee.getLastName());
    }

    public int compareBySalary(Employee employee) {
        double diff = this.salary - employee.getSalary();
        if (diff > PRECISION) return 1;
        else if (diff < -PRECISION) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
                birthYear == employee.birthYear &&
                Double.compare(employee.salary, salary) == 0 &&
                Objects.equals(firstName, employee.firstName) &&
                Objects.equals(lastName, employee.lastName) &&
                condition == employee.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, condition, birthYear, salary);
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName +
                ", condition: " + condition +
                ", birth year: " + birthYear +
                ", salary: " + salary;
    }

    public String getConditionToString() {
        return String.valueOf(condition);
    }
}
