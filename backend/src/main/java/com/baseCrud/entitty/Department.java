package com.baseCrud.entitty;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String note;

    @ManyToOne
    @JoinColumn(name = "parent_id")
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
