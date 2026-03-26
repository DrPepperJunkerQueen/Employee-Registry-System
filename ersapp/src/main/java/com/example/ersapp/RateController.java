package com.example.piec;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RateController {

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private ClassEmployeeRepository classEmployeeRepository;

    @PostMapping("/rates")
    public ResponseEntity<Rate> addRate(@RequestBody RateDTO rateDTO) {
        // Krok 1: Szukamy grupy po ID
        Optional<ClassEmployee> groupOpt = classEmployeeRepository.findById(rateDTO.getGroupId());

        if (groupOpt.isEmpty()) {
            // Jeśli nie znaleziono grupy o takim ID – zwróć błąd 400
            return ResponseEntity.badRequest().build();
        }

        ClassEmployee group = groupOpt.get();

        // Krok 2: Tworzymy nową ocenę i ustawiamy jej wartość i przypisaną grupę
        Rate rate = new Rate();
        rate.setGrade(rateDTO.getGrade());
        rate.setGroup(group);

        // Krok 3: Dodajemy ocenę do listy ocen w grupie (opcjonalnie, jeśli masz `mappedBy`)
        group.getRates().add(rate);

        // Krok 4: Zapisujemy ocenę do bazy
        rateRepository.save(rate);

        // Zwracamy ocenę z odpowiedzią 200 OK
        return ResponseEntity.ok(rate);
    }
}