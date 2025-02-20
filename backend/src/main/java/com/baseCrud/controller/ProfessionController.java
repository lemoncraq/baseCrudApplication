package com.baseCrud.controller;

import com.baseCrud.entitty.Profession;
import com.baseCrud.service.ProfessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/professions")
public class ProfessionController {
    private final ProfessionService professionService;

    public ProfessionController(ProfessionService professionService) {
        this.professionService = professionService;
    }

    @GetMapping
    public Page<Profession> getAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return professionService.getAllProfessions(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profession> getById(@PathVariable UUID id) {
        return professionService.getProfessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profession> create(@RequestBody Profession profession) {
        return ResponseEntity.ok(professionService.createProfession(profession));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profession> update(@PathVariable UUID id, @RequestBody Profession updatedProfession) {
        return professionService.updateProfession(id, updatedProfession)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return professionService.deleteProfession(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

