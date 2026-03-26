package com.example.rest;

import com.example.rest.ClassEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassEmployeeRepository extends JpaRepository<ClassEmployee, Long> {
    // Możesz tu dodać dodatkowe metody wyszukiwania jeśli potrzebujesz
}
