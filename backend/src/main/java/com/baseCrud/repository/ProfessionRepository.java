package com.baseCrud.repository;

import com.baseCrud.entitty.Profession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessionRepository extends JpaRepository<Profession, UUID> {
    Page<Profession> findAll(Pageable pageable);
}