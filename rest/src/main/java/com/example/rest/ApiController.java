package com.example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RateService rateService;

    // --- EMPLOYEE ---

    @PostMapping("/employee")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO saved = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee((long) id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/csv")
    public ResponseEntity<String> getEmployeesAsCSV() {
        String csv = Arrays.toString(employeeService.generateEmployeeCSV());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "employees.csv");
        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }

    // --- GROUP ---

    @GetMapping("/group")
    public ResponseEntity<List<ClassEmployee>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @PostMapping("/group")
    public ResponseEntity<ClassEmployeeDTO> addGroup(@RequestBody ClassEmployeeDTO dto) {
        ClassEmployeeDTO saved = groupService.createGroup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group/{id}/employee")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesInGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getEmployeesByGroupId(id));
    }

    @GetMapping("/group/{id}/fill")
    public ResponseEntity<Double> getGroupFillPercentage(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupFillPercentage(id));
    }

    // --- RATING ---

    @PostMapping("/rating")
    public ResponseEntity<RateDTO> addRating(@RequestBody RateDTO dto) {
        RateDTO saved = rateService.createRate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
