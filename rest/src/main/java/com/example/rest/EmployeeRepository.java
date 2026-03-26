package com.example.rest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByGroup_Id(Long groupId);

    long countByGroup_Id(Long groupId);
}
