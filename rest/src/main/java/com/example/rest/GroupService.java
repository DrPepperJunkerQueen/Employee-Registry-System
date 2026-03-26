package com.example.rest;

import com.example.rest.ClassEmployee;
import com.example.rest.ClassEmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final ClassEmployeeRepository groupRepository;
    private final EmployeeRepository employeeRepository;

    public GroupService(ClassEmployeeRepository groupRepository, EmployeeRepository employeeRepository) {
        this.groupRepository = groupRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<ClassEmployee> getAllGroups() {
        return groupRepository.findAll();
    }

    public ClassEmployee addGroup(ClassEmployee group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        if (!groupRepository.existsById(id)) {
            throw new RuntimeException("Group not found");
        }
        groupRepository.deleteById(id);
    }

    public int getFillPercentage(Long id) {
        return (int) groupRepository.findById(id).map(ClassEmployee::getFillPercentage)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    public ClassEmployeeDTO createGroup(ClassEmployeeDTO dto) {
        ClassEmployee group = new ClassEmployee();
        group.setEmployeeGroupName(dto.getEmployeeGroupName());
        group.setMaxEmployeeNumber(dto.getMaxEmployeeNumber());

        ClassEmployee saved = groupRepository.save(group);
        ClassEmployeeDTO result = new ClassEmployeeDTO();
        result.setId(saved.getId());
        result.setEmployeeGroupName(saved.getEmployeeGroupName());
        result.setMaxEmployeeNumber(saved.getMaxEmployeeNumber());
        return result;
    }

    public List<EmployeeDTO> getEmployeesByGroupId(Long groupId) {
        List<Employee> employees = employeeRepository.findByGroup_Id(groupId);
        return employees.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public double getGroupFillPercentage(Long groupId) {
        ClassEmployee group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        int max = group.getMaxEmployeeNumber();
        long count = employeeRepository.countByGroup_Id(groupId);

        return max == 0 ? 0 : (double) count * 100.0 / max;
    }

    private EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(Long.valueOf(employee.getId()));
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setBirthYear(employee.getBirthYear());
        dto.setSalary(employee.getSalary());
        dto.setCondition(employee.getCondition());
        dto.setGroupId(employee.getGroup() != null ? employee.getGroup().getId() : null);
        return dto;
    }

}