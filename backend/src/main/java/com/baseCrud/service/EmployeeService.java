package com.baseCrud.service;

import com.baseCrud.entitty.Employee;
import com.baseCrud.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> updateEmployee(UUID id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFullName(updatedEmployee.getFullName());
            employee.setNote(updatedEmployee.getNote());
            employee.setDepartment(updatedEmployee.getDepartment());
            employee.setProfession(updatedEmployee.getProfession());
            return employeeRepository.save(employee);
        });
    }

    public boolean deleteEmployee(UUID id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
