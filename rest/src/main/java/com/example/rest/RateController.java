package com.example.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@CrossOrigin(origins = "*")
public class RateController {

    private final RateService rateService;
    private final ClassEmployeeRepository classEmployeeRepository;
    private final RateRepository rateRepository;

    public RateController(RateService rateService,
                          ClassEmployeeRepository classEmployeeRepository,
                          RateRepository rateRepository) {
        this.rateService = rateService;
        this.classEmployeeRepository = classEmployeeRepository;
        this.rateRepository = rateRepository;
    }

    @GetMapping
    public ResponseEntity<List<RateDTO>> getAllRates() {
        return ResponseEntity.ok(rateService.getAllRates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateDTO> getRateById(@PathVariable int id) {
        return rateService.getRateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<RateDTO>> getRatesByGroupId(@PathVariable Long groupId) {
        return ResponseEntity.ok(rateService.getRatesByGroupId(groupId));
    }

    @PostMapping
    public ResponseEntity<RateDTO> createRate(@RequestBody RateDTO dto) {
        return ResponseEntity.ok(rateService.createRate(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable int id) {
        rateService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }

    // ⬇⬇⬇ DODANY ENDPOINT DO MASOWEGO ZAPISU ⬇⬇⬇
    @PostMapping("/bulk")
    @Transactional
    public ResponseEntity<?> saveRates(@RequestBody RateWrapper wrapper) {
        List<Rate> rates = wrapper.getRates();

        for (Rate rate : rates) {
            if (rate.getGroup() == null && rate.getGroupId() != null) {
                ClassEmployee group = classEmployeeRepository.findById(Long.valueOf(rate.getGroupId()))
                        .orElseThrow(() -> new RuntimeException("Group not found for ID: " + rate.getGroupId()));
                rate.setGroup(group);
            }
        }

        rateRepository.saveAll(rates);
        return ResponseEntity.ok("Rates saved successfully");
    }
}
