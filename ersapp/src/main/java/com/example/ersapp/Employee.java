package com.example.ersapp;

import jakarta.persistence.*;
import java.time.Year;
import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee implements Printing, Comparable<Employee>
{

    private static final double PRECISION = 1e-2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
        // pusty konstruktor wymagany przez JPA
    }

    public Employee(String firstName, String lastName, EmployeeCondition condition, int birthYear, double salary) {
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("First name can't be blank.");
        }
        if (lastName.isEmpty()) {
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

    // Gettery, settery, equals, hashCode, compareTo... (pozostają bez zmian)


    @Override
    public void print()
    {
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Condition: " + condition);
        System.out.println("Birth year: " + birthYear);
        System.out.println("Salary: $" + salary);
    }

    @Override
    public int compareTo(Employee employee)
    {
        return (this.lastName.compareTo(employee.getLastName()));
    }

    public int compareBySalary(Employee employee)
    {
        if (this.salary - employee.getSalary() > PRECISION)
            return 1;
        else if (this.salary - employee.getSalary() < PRECISION)
            return -1;
        return 0;


    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setCondition(EmployeeCondition condition)
    {
        this.condition = condition;
    }

    public void setBirthYear(int birthYear)
    {
        this.birthYear = birthYear;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }

    public void setGroup(ClassEmployee group) {this.group = group;}

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public EmployeeCondition getCondition()
    {
        return condition;
    }

    public int getBirthYear()
    {
        return birthYear;
    }

    public double getSalary()
    {
        return salary;
    }

    public ClassEmployee getGroup() {return group;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
        return id + " " + firstName + " " + lastName + ", condition: " + condition + ", birth year: " + birthYear + ", salary: " + salary;
    }

}
