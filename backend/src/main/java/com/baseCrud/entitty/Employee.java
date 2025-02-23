package com.baseCrud.entitty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "ФИО обязательно")
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = true)
    private String note;

    @NotNull(message = "Профессия обязательна")
    @ManyToOne(optional = false)
    @JoinColumn(name = "profession_id", nullable = false)
    private Profession profession;

    @NotNull(message = "Отдел обязателен")
    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    public Employee() {
    }

    public Employee(UUID id, String fullName, String note, Profession profession, Department department) {
        this.id = id;
        this.fullName = fullName;
        this.note = note;
        this.profession = profession;
        this.department = department;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
