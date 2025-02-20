package com.baseCrud.service;

import com.baseCrud.entitty.Department;
import com.baseCrud.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Page<Department> getAllDepartments(Pageable pageable) {
        return  departmentRepository.findAll(pageable);
    }

    public Optional<Department> getDepartmentById(UUID id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Optional<Department> updateDepartment(UUID id, Department updatedDepartment) {
        return departmentRepository.findById(id).map(department -> {
            department.setName(updatedDepartment.getName());
            department.setNote(updatedDepartment.getNote());
            department.setParentDepartment(updatedDepartment.getParentDepartment());
            return departmentRepository.save(department);
        });
    }

    public boolean deleteDepartment(UUID id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
