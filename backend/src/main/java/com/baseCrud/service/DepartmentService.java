package com.baseCrud.service;

import com.baseCrud.dto.DepartmentDto;
import com.baseCrud.entitty.Department;
import com.baseCrud.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Page<DepartmentDto> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(DepartmentDto::new);
    }

    public Optional<DepartmentDto> getDepartmentById(UUID id) {
        return departmentRepository.findById(id).map(DepartmentDto::new);
    }

    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = toEntity(departmentDto);
        return new DepartmentDto(departmentRepository.save(department));
    }

    public Optional<DepartmentDto> updateDepartment(UUID id, DepartmentDto updatedDto) {
        return departmentRepository.findById(id).map(department -> {
            department.setName(updatedDto.getName());
            department.setNote(updatedDto.getNote());
            if (updatedDto.getParentDepartmentId() != null) {
                department.setParentDepartment(departmentRepository.findById(updatedDto.getParentDepartmentId()).orElse(null));
            }
            return new DepartmentDto(departmentRepository.save(department));
        });
    }

    public boolean deleteDepartment(UUID id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Department toEntity(DepartmentDto dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setNote(dto.getNote());

        if (dto.getParentDepartmentId() != null) {
            Department parentDepartment = departmentRepository.findById(dto.getParentDepartmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Родительский отдел не найден для id " + dto.getParentDepartmentId()));
            department.setParentDepartment(parentDepartment);
        }

        return department;
    }
}
