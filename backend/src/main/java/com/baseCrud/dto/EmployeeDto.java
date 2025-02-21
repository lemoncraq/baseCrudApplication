package com.baseCrud.dto;

import com.baseCrud.entitty.Employee;

import java.util.UUID;

public class EmployeeDto {
    private UUID id;
    private String fullName;
    private UUID professionId;
    private UUID departmentId;
    private String note;

    public EmployeeDto() {}

    public EmployeeDto(UUID id, String fullName, UUID professionId, UUID departmentId, String note) {
        this.id = id;
        this.fullName = fullName;
        this.professionId = professionId;
        this.departmentId = departmentId;
        this.note = note;
    }

    public EmployeeDto(Employee employee) {
        this(employee.getId(), employee.getFullName(), employee.getProfession().getId(), employee.getDepartment().getId(), employee.getNote());
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

    public UUID getProfessionId() {
        return professionId;
    }

    public void setProfessionId(UUID professionId) {
        this.professionId = professionId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
