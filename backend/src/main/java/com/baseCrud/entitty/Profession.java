package com.baseCrud.entitty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Наименование обязательно")
    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String note;

    public Profession() {
    }

    public Profession(UUID id, String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
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