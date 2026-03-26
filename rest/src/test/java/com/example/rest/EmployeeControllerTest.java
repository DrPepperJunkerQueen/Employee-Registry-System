package com.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ClassEmployeeRepository classEmployeeRepository;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
        classEmployeeRepository.deleteAll();
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        ClassEmployee group = classEmployeeRepository.save(new ClassEmployee("Group A", 10));

        Employee employee = new Employee("Jan", "Kowalski", 1990, "PRESENT", 4000);
        employee.setGroup(group);

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jan"));
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {
        ClassEmployee group = classEmployeeRepository.save(new ClassEmployee("Group B", 5));
        Employee emp = employeeRepository.save(new Employee("Anna", "Nowak", 1985, "SICK_LEAVE", 3000));
        group.addEmployee(emp);

        mockMvc.perform(delete("/api/employee/" + emp.getId()))
                .andExpect(status().isOk());

        Assertions.assertFalse(employeeRepository.findById(emp.getId()).isPresent());
    }

    @Test
    public void shouldReturnCSV() throws Exception {
        mockMvc.perform(get("/api/employee/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"));
    }
}

