package com.baseCrud.dto;

import com.baseCrud.entitty.Department;

import java.util.UUID;

public class DepartmentDto {
    private UUID id;
    private String name;
    private String note;
    private UUID parentDepartmentId;

    public DepartmentDto() {}

    public DepartmentDto(UUID id, String name, String note, UUID parentDepartmentId) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.parentDepartmentId = parentDepartmentId;
    }

    public DepartmentDto(Department department) {
        this(department.getId(), department.getName(), department.getNote(), department.getParentDepartment().getId());
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

    public UUID getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(UUID parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }
}
