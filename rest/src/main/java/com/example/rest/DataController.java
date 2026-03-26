package com.example.rest;

import com.example.rest.GroupDataDTO;
import com.example.rest.ClassEmployee;
import com.example.rest.ClassEmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private ClassEmployeeRepository classEmployeeRepository;

    @Autowired
    private RateRepository rateRepository;

    @PostMapping("/complete")
    public ResponseEntity<String> receiveCompleteData(@RequestBody GroupDataDTO data) {
        System.out.println("ODEBRANE DANE:");
        System.out.println(data);
        try {
            for (ClassEmployee group : data.getGroups()) {
                // przypisz grupę do każdego pracownika
                if (group.getEmployeeList() != null) {
                    for (Employee emp : group.getEmployeeList()) {
                        emp.setGroup(group);
                    }
                }
                if (group.getRates() != null) {
                    for (Rate rate : group.getRates()) {
                        rate.setGroup(group);
                    }
                }

                // DEBUG: wypisz pracowników i ich dane
                System.out.println("Grupa: " + group.getEmployeeGroupName());
                if (group.getEmployeeList() != null) {
                    System.out.println("Pracownicy w grupie: " + group.getEmployeeList().size());
                    for (Employee emp : group.getEmployeeList()) {
                        System.out.println(" -> " + emp.getFirstName() + " " + emp.getLastName());
                    }
                } else {
                    System.out.println("Brak pracowników w tej grupie.");
                }

                classEmployeeRepository.save(group);
            }

            return ResponseEntity.ok("Complete data received and saved.");
        } catch (Exception e) {
            e.printStackTrace();  // <--- WAŻNE
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}

