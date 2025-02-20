package com.baseCrud.service;

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


    public Page<Profession> getAllProfessions(Pageable pageable) {
        return professionRepository.findAll(pageable);
    }

    public Optional<Profession> getProfessionById(UUID id) {
        return professionRepository.findById(id);
    }

    public Profession createProfession(Profession profession) {
        return professionRepository.save(profession);
    }

    public Optional<Profession> updateProfession(UUID id, Profession updatedProfession) {
        return professionRepository.findById(id).map(profession -> {
            profession.setName(updatedProfession.getName());
            profession.setNote(updatedProfession.getNote());
            return professionRepository.save(profession);
        });
    }

    public boolean deleteProfession(UUID id) {
        if (professionRepository.existsById(id)) {
            professionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
