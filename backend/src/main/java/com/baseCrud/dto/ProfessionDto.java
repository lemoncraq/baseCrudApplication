package com.baseCrud.dto;

import com.baseCrud.entitty.Profession;

import java.util.UUID;

public class ProfessionDto {
    private UUID id;
    private String name;
    private String note;

    public ProfessionDto() {}

    public ProfessionDto(UUID id, String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
    }

    public ProfessionDto(Profession profession) {
        this(profession.getId(), profession.getName(), profession.getNote());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
