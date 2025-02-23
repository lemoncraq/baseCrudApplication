package com.baseCrud.controller;

import com.baseCrud.dto.DepartmentDto;
import com.baseCrud.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAll() {
        return ResponseEntity.ok(
                departmentService.getAllDepartments()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestBody @Valid DepartmentDto department) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(
            @PathVariable UUID id,
            @RequestBody @Valid DepartmentDto updatedDepartment) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, updatedDepartment));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
    }

}
