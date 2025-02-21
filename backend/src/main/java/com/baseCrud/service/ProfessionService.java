package com.baseCrud.service;

import com.baseCrud.dto.ProfessionDto;
import com.baseCrud.entitty.Profession;
import com.baseCrud.repository.ProfessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessionService {
    private final ProfessionRepository professionRepository;

    public ProfessionService(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    public Page<ProfessionDto> getAllProfessions(Pageable pageable) {
        return professionRepository.findAll(pageable).map(ProfessionDto::new);
    }

    public Optional<ProfessionDto> getProfessionById(UUID id) {
        return professionRepository.findById(id).map(ProfessionDto::new);
    }

    public ProfessionDto createProfession(ProfessionDto professionDto) {
        Profession profession = toEntity(professionDto);
        return new ProfessionDto(professionRepository.save(profession));
    }

    public Optional<ProfessionDto> updateProfession(UUID id, ProfessionDto updatedProfessionDto) {
        return professionRepository.findById(id).map(profession -> {
            profession.setName(updatedProfessionDto.getName());
            profession.setNote(updatedProfessionDto.getNote());
            return new ProfessionDto(professionRepository.save(profession));
        });
    }

    public boolean deleteProfession(UUID id) {
        if (professionRepository.existsById(id)) {
            professionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Profession toEntity(ProfessionDto professionDto) {
        return new Profession(professionDto.getId(), professionDto.getName(), professionDto.getNote());
    }
}
