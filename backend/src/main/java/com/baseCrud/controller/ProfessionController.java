package com.baseCrud.controller;

import com.baseCrud.dto.ProfessionDto;
import com.baseCrud.service.ProfessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/professions")
public class ProfessionController {
    private final ProfessionService professionService;

    public ProfessionController(ProfessionService professionService) {
        this.professionService = professionService;
    }

    @GetMapping
    public ResponseEntity<List<ProfessionDto>> getAll() {
        return ResponseEntity.ok(
                professionService.getAllProfessions()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(professionService.getProfessionById(id));
    }

    @PostMapping
    public ResponseEntity<ProfessionDto> create(@RequestBody @Valid ProfessionDto profession) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professionService.createProfession(profession));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionDto> update(
            @PathVariable UUID id,
            @RequestBody @Valid ProfessionDto updatedProfession) {
        return ResponseEntity.ok(professionService.updateProfession(id, updatedProfession));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        professionService.deleteProfession(id);
    }
}

