package com.example.rest;

import com.example.rest.EmployeeDTO;
import com.example.rest.ClassEmployee;
import com.example.rest.Employee;
import com.example.rest.ClassEmployeeRepository;
import com.example.rest.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ClassEmployeeRepository groupRepository;

    public EmployeeService(EmployeeRepository employeeRepository, ClassEmployeeRepository groupRepository) {
        this.employeeRepository = employeeRepository;
        this.groupRepository = groupRepository;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(Math.toIntExact(id)).map(this::toDTO);
    }

    public EmployeeDTO addEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setCondition(dto.getCondition());
        employee.setBirthYear(dto.getBirthYear());
        employee.setSalary(dto.getSalary());

        if (dto.getGroupId() != null) {
            ClassEmployee group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
            employee.setGroup(group);
        }

        return toDTO(employeeRepository.save(employee));
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(Math.toIntExact(id));
    }

    public List<EmployeeDTO> getEmployeesByGroupId(Long groupId) {
        return employeeRepository.findByGroup_Id(groupId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private EmployeeDTO toDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(Long.valueOf(e.getId()));
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setCondition(e.getCondition());
        dto.setBirthYear(e.getBirthYear());
        dto.setSalary(e.getSalary());
        dto.setGroupId(e.getGroup() != null ? e.getGroup().getId() : null);
        return dto;
    }

    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        ClassEmployee group = null;
        if (dto.getGroupId() != null) {
            group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
        }

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setBirthYear(dto.getBirthYear());
        employee.setSalary(dto.getSalary());
        employee.setCondition(dto.getCondition());
        employee.setGroup(group);

        return toDTO(employeeRepository.save(employee));
    }

    public byte[] generateEmployeeCSV() {
        List<Employee> employees = employeeRepository.findAll();
        StringBuilder sb = new StringBuilder("ID,FirstName,LastName,Condition,BirthYear,Salary,GroupId\n");

        for (Employee e : employees) {
            sb.append(e.getId()).append(",");
            sb.append(e.getFirstName()).append(",");
            sb.append(e.getLastName()).append(",");
            sb.append(e.getCondition()).append(",");
            sb.append(e.getBirthYear()).append(",");
            sb.append(e.getSalary()).append(",");
            sb.append(e.getGroup() != null ? e.getGroup().getId() : "").append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}