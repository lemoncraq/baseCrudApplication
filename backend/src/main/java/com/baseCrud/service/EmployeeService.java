package com.baseCrud.service;

import com.baseCrud.dto.EmployeeDto;
import com.baseCrud.entitty.Department;
import com.baseCrud.entitty.Employee;
import com.baseCrud.entitty.Profession;
import com.baseCrud.repository.DepartmentRepository;
import com.baseCrud.repository.EmployeeRepository;
import com.baseCrud.repository.ProfessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ProfessionRepository professionRepository;
    private final DepartmentRepository departmentRepository;


    public EmployeeService(EmployeeRepository employeeRepository, ProfessionRepository professionRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.professionRepository = professionRepository;
        this.departmentRepository = departmentRepository;
    }


    public Page<EmployeeDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeDto::new);
    }

    public Optional<EmployeeDto> getEmployeeById(UUID id) {
        return employeeRepository.findById(id).map(EmployeeDto::new);
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = toEntity(employeeDto);
        return new EmployeeDto(employeeRepository.save(employee));
    }

    public Optional<EmployeeDto> updateEmployee(UUID id, EmployeeDto updatedDto) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFullName(updatedDto.getFullName());
            employee.setNote(updatedDto.getNote());
            if (updatedDto.getProfessionId() != null) {
                employee.setProfession(professionRepository.findById(updatedDto.getProfessionId()).orElse(null));
            }
            if (updatedDto.getDepartmentId() != null) {
                employee.setDepartment(departmentRepository.findById(updatedDto.getDepartmentId()).orElse(null));
            }
            return new EmployeeDto(employeeRepository.save(employee));
        });
    }

    public boolean deleteEmployee(UUID id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFullName(dto.getFullName());
        employee.setNote(dto.getNote());
        if (dto.getProfessionId() != null) {
            Profession profession = professionRepository.findById(dto.getProfessionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Профессия не найдена для id " + dto.getProfessionId()));
            employee.setProfession(profession);
        }

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Отдел не найден для id " + dto.getDepartmentId()));
            employee.setDepartment(department);
        }
        return employee;
    }
}
