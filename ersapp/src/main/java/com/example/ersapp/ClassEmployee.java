package com.example.piec;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "employee_groups")
public class ClassEmployee
{
    @Id
    @GeneratedValue
    private Long id;

    private String employeeGroupName;
    private int maxEmployeeNumber;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employeeList = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Rate> rates;

    public ClassEmployee() {

    }

    public ClassEmployee(String employeeGroupName, int maxEmployeeNumber, List<Employee> employeeList)
    {
        // Sprawdzanie poprawności argumentów
        if(employeeGroupName == null || employeeGroupName.trim().isEmpty())
        {
            throw new IllegalArgumentException("Group name can't be blank.");
        }
        if(maxEmployeeNumber <= 0)
        {
            throw new IllegalArgumentException("Max employee must be positive.");
        }

        this.employeeGroupName = employeeGroupName;
        this.maxEmployeeNumber = maxEmployeeNumber;

        this.employeeList = (employeeList != null) ? new ArrayList<>(employeeList) : new ArrayList<>();
        this.rates = new ArrayList<>();
    }

    public ClassEmployee(String employeeGroupName, int maxEmployeeNumber, List<Employee> employeeList, List<Rate> rates)
    {
        // Sprawdzanie poprawności argumentów
        if(employeeGroupName == null || employeeGroupName.trim().isEmpty())
        {
            throw new IllegalArgumentException("Group name can't be blank.");
        }
        if(maxEmployeeNumber <= 0)
        {
            throw new IllegalArgumentException("Max employee must be positive.");
        }

        this.employeeGroupName = employeeGroupName;
        this.maxEmployeeNumber = maxEmployeeNumber;

        this.employeeList = (employeeList != null) ? new ArrayList<>(employeeList) : new ArrayList<>();

        this.rates = rates;
    }

    public void addSalary(Employee employee, double salaryAddition)
    {
        employee.setSalary(employee.getSalary() + salaryAddition);
    }


    public void changeCondition(Employee employee, EmployeeCondition condition)
    {
        employee.setCondition(condition);
    }

    public ArrayList<Employee> search(String lastName)
    {
        int size = this.employeeList.size();
        ArrayList<Employee> array = new ArrayList<>();
        Employee temp = new Employee("temp", lastName, EmployeeCondition.ABSENT, 0, 1.0);
        temp.setLastName(lastName);

        for(int i=0; i<size; i++)
        {
            if(employeeList.get(i).compareTo(temp) == 0)
            {
                array.add(employeeList.get(i));
            }
        }

        return array;
    }

    public ArrayList<Employee> searchPartial(String text)
    {
        int size = this.employeeList.size();
        ArrayList<Employee> array = new ArrayList<>();

        for(int i=0; i<size; i++)
        {
            if(employeeList.get(i).getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                    employeeList.get(i).getLastName().toLowerCase().contains(text.toLowerCase()))
            {
                array.add(employeeList.get(i));
            }
        }

        return array;
    }

    public int countByCondition (EmployeeCondition condition)
    {
        int size = this.employeeList.size();
        int count = 0;

        for(int i=0; i<size; i++)
        {
            if(employeeList.get(i).getCondition() == condition)
            {
                count++;
            }
        }

        return count;
    }

    public void summary()
    {
        System.out.println("Group name: " + employeeGroupName);
        System.out.println("Max employee number: " + maxEmployeeNumber);
        System.out.println("Fill percentage: " + getFillPercentage() + "%");
        int size = this.employeeList.size();

        for(int i=0; i<size; i++)
        {
            employeeList.get(i).print();
            System.out.println("\n");
        }
    }

    public List<Employee> sortByLastName()
    {
        int size = this.employeeList.size();

        for(int i=0; i<size - 1; i++)
        {
            for(int j=0; j<size - 1 - i; j++)
            {
                if(employeeList.get(j).compareTo(employeeList.get(j+1)) > 0)
                {
                    Employee temp = employeeList.get(j);
                    employeeList.set(j, employeeList.get(j+1));
                    employeeList.set(j+1, temp);
                }
            }
        }

        return employeeList;
    }

    public List<Employee> sortBySalary()
    {
        int size = this.employeeList.size();

        for(int i=0; i<size - 1; i++)
        {
            for(int j=0; j<size - 1 - i; j++)
            {
                if(employeeList.get(j).compareBySalary(employeeList.get(j+1)) < 0)
                {
                    Employee temp = employeeList.get(j);
                    employeeList.set(j, employeeList.get(j+1));
                    employeeList.set(j+1, temp);
                }
            }
        }

        return employeeList;
    }

    public Employee maxEmployee()
    {
        return employeeList.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(Employee::getLastName))
                .orElse(null);

    }

    public boolean isEmpty()
    {
        return employeeList.size() == 0;
    }

    public Long getId() {return  id;}

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
        // Never return null, return empty list instead
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        // Safety check to avoid setting null
        this.employeeList = (employeeList != null) ? new ArrayList<>(employeeList) : new ArrayList<>();
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

    public boolean removeEmployee(Employee employee) {
        if (employee == null) {
            return false;
        }

        // Initialize list if needed
        if (employeeList == null) {
            return false;  // Nothing to remove from an empty list
        }

        return employeeList.remove(employee);
    }

    public int getEmployeeNumber() {
        // Check if employeeList is null
        if (employeeList == null) {
            return 0;
        }
        return employeeList.size();
    }

    public double getFillPercentage() {
        // Safely get employee count and avoid division by zero
        if (maxEmployeeNumber <= 0) {
            return 0.0;
        }

        int employeeCount = getEmployeeNumber();
        return ((double) employeeCount / maxEmployeeNumber) * 100.0;
    }

    public int getMaxID() {
        // Fix for line 297: Check if employeeList is null
        if (employeeList == null) {
            return 0;
        }

        int maxID = 0;
        for (Employee emp : employeeList) {
            if (emp != null && emp.getId() > maxID) {
                maxID = emp.getId();
            }
        }
        return maxID;
    }

    public void setRates(List<Rate> rates) {this.rates = rates;}
    public List<Rate> getRates() {return rates;}

    public void addRate(Rate rate) {
        if (rate == null) return;

        // Set the bidirectional relationship
        rate.setGroup(this);

        // Initialize the rates collection if needed
        if (this.rates == null) {
            this.rates = new ArrayList<>();
        }

        // Add to the collection
        this.rates.add(rate);
    }

    public double getAverageGrade()
    {
        double sum = 0.0;
        for (Rate rate : this.rates)
        {
            sum += Double.valueOf(rate.getGrade());
        }
        return sum / this.employeeList.size();
    }

    public int getMaxRateID() {
        // Fix for line 297: Check if employeeList is null
        if (this.rates == null) {
            return 0;
        }

        int maxID = 0;
        for (Rate rt : this.rates) {
            if (rt != null && rt.getId() > maxID) {
                maxID = rt.getId();
            }
        }
        return maxID;
    }

    public boolean removeRate(Rate rate) {
        if (rate == null) {
            return false;
        }

        // Initialize list if needed
        if (rates == null) {
            return false;  // Nothing to remove from an empty list
        }

        return rates.remove(rate);
    }
}
