package com.baseCrud.controller;

import com.baseCrud.dto.EmployeeDto;
import com.baseCrud.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody @Valid EmployeeDto employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(
            @PathVariable UUID id,
            @RequestBody @Valid EmployeeDto updatedEmployee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, updatedEmployee));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }
}
