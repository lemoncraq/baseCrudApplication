package com.baseCrud.entitty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Наименование обязательно")
    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String note;

    @ManyToOne(optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    private Department parentDepartment;

    public Department() {
    }

    public Department(UUID id, String name, String note, Department parentDepartment) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.parentDepartment = parentDepartment;
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

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }
}
