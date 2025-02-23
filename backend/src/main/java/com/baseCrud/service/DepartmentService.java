package com.baseCrud.service;

import com.baseCrud.dto.DepartmentDto;
import com.baseCrud.entitty.Department;
import com.baseCrud.repository.DepartmentRepository;
import com.baseCrud.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map(DepartmentDto::new).toList();
    }

    public DepartmentDto getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .map(DepartmentDto::new)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с ID " + id + " не найден"));
    }

    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = toEntity(departmentDto);
        return new DepartmentDto(departmentRepository.save(department));
    }

    @Transactional
    public DepartmentDto updateDepartment(UUID id, DepartmentDto updatedDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с ID " + id + " не найден"));

        department.setName(updatedDto.getName());
        department.setNote(updatedDto.getNote());

        if (updatedDto.getParentDepartmentId() != null) {
            Department parent = departmentRepository.findById(updatedDto.getParentDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Родительский отдел с ID " + updatedDto.getParentDepartmentId() + " не найден"));
            department.setParentDepartment(parent);
        } else {
            department.setParentDepartment(null);
        }

        return new DepartmentDto(departmentRepository.save(department));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с ID " + id + " не найден"));
        long relatedRecords = employeeRepository.countByDepartmentId(id);
        long childCount = departmentRepository.countByParentDepartment(department);
        if (childCount + relatedRecords > 0) {
            throw new IllegalStateException("Невозможно удалить отдел с ID " + id + ", так как существуют связанные записи");
        }
        departmentRepository.delete(department);
    }

    private Department toEntity(DepartmentDto dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setNote(dto.getNote());

        if (dto.getParentDepartmentId() != null) {
            Department parentDepartment = departmentRepository.findById(dto.getParentDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Родительский отдел не найден для id " + dto.getParentDepartmentId()));
            department.setParentDepartment(parentDepartment);
        }

        return department;
    }
}
