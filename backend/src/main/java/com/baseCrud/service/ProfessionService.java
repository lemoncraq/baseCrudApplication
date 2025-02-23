package com.baseCrud.service;

import com.baseCrud.dto.ProfessionDto;
import com.baseCrud.entitty.Profession;
import com.baseCrud.repository.EmployeeRepository;
import com.baseCrud.repository.ProfessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProfessionService {
    private final ProfessionRepository professionRepository;

    private final EmployeeRepository employeeRepository;

    public ProfessionService(ProfessionRepository professionRepository,
                             EmployeeRepository employeeRepository) {
        this.professionRepository = professionRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<ProfessionDto> getAllProfessions() {
        return professionRepository.findAll().stream().map(ProfessionDto::new).toList();
    }

    public ProfessionDto getProfessionById(UUID id) {
        return professionRepository.findById(id)
                .map(ProfessionDto::new)
                .orElseThrow(() -> new EntityNotFoundException("Профессия с ID " + id + " не найдена"));
    }

    @Transactional
    public ProfessionDto createProfession(ProfessionDto professionDto) {
        Profession profession = toEntity(professionDto);
        return new ProfessionDto(professionRepository.save(profession));
    }

    @Transactional
    public ProfessionDto updateProfession(UUID id, ProfessionDto updatedProfessionDto) {
        Profession profession = professionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Профессия с ID " + id + " не найдена"));

        profession.setName(updatedProfessionDto.getName());
        profession.setNote(updatedProfessionDto.getNote());

        return new ProfessionDto(professionRepository.save(profession));
    }

    @Transactional
    public void deleteProfession(UUID id) {
        Profession profession = professionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Профессия с ID " + id + " не найдена"));
        long relatedRecords = employeeRepository.countByProfessionId(id);
        if (relatedRecords > 0) {
            throw new IllegalStateException("Невозможно удалить профессию с ID " + id + ", так как существуют связанные записи");
        }
        professionRepository.delete(profession);
    }

    private Profession toEntity(ProfessionDto professionDto) {
        return new Profession(professionDto.getId(), professionDto.getName(), professionDto.getNote());
    }
}
