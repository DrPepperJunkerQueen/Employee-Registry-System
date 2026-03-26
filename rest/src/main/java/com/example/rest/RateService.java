package com.example.rest;

import com.example.rest.ClassEmployee;
import com.example.rest.ClassEmployeeRepository;
import com.example.rest.Rate;
import com.example.rest.RateDTO;
import com.example.rest.RateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final ClassEmployeeRepository groupRepository;

    public RateService(RateRepository rateRepository, ClassEmployeeRepository groupRepository) {
        this.rateRepository = rateRepository;
        this.groupRepository = groupRepository;
    }

    public List<RateDTO> getAllRates() {
        return rateRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<RateDTO> getRateById(int id) {
        return rateRepository.findById(id)
                .map(this::mapToDTO);
    }

    public List<RateDTO> getRatesByGroupId(Long groupId) {
        return rateRepository.findByGroup_Id(groupId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RateDTO createRate(RateDTO dto) {
        // Znajdź grupę po ID
        ClassEmployee group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Stwórz Rate
        Rate rate = new Rate();
        rate.setGrade(dto.getGrade());
        rate.setGroup(group); // TO JEST KLUCZOWE!
        rate.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
        rate.setComment(dto.getComment() != null ? dto.getComment() : "");

        // Zapisz i zwróć DTO
        Rate saved = rateRepository.save(rate);
        return mapToDTO(saved);
    }


    public void deleteRate(int id) {
        rateRepository.deleteById(id);
    }

    private RateDTO mapToDTO(Rate rate) {
        RateDTO dto = new RateDTO();
        dto.setId(rate.getId());
        dto.setGrade(rate.getGrade());
        dto.setGroupId(rate.getGroup() != null ? rate.getGroup().getId() : null);
        dto.setDate(rate.getDate());
        dto.setComment(rate.getComment());
        return dto;
    }
}
