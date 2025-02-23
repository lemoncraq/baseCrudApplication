package com.baseCrud.service;

import com.baseCrud.dto.EmployeeDto;
import com.baseCrud.entitty.Department;
import com.baseCrud.entitty.Employee;
import com.baseCrud.entitty.Profession;
import com.baseCrud.repository.DepartmentRepository;
import com.baseCrud.repository.EmployeeRepository;
import com.baseCrud.repository.ProfessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream().map(EmployeeDto::new).toList();
    }

    public EmployeeDto getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .map(EmployeeDto::new)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с ID " + id + " не найден"));
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = toEntity(employeeDto);
        return new EmployeeDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto updateEmployee(UUID id, EmployeeDto updatedDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с ID " + id + " не найден"));

        employee.setFullName(updatedDto.getFullName());
        employee.setNote(updatedDto.getNote());

        if (updatedDto.getProfessionId() != null) {
            Profession profession = professionRepository.findById(updatedDto.getProfessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Профессия с ID " + updatedDto.getProfessionId() + " не найдена"));
            employee.setProfession(profession);
        }

        if (updatedDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(updatedDto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Отдел с ID " + updatedDto.getDepartmentId() + " не найден"));
            employee.setDepartment(department);
        }

        return new EmployeeDto(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с ID " + id + " не найден"));

        employeeRepository.delete(employee);
    }
    private Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFullName(dto.getFullName());
        employee.setNote(dto.getNote());
        if (dto.getProfessionId() != null) {
            Profession profession = professionRepository.findById(dto.getProfessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Профессия не найдена для id " + dto.getProfessionId()));
            employee.setProfession(profession);
        }

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Отдел не найден для id " + dto.getDepartmentId()));
            employee.setDepartment(department);
        }
        return employee;
    }
}
