package com.baseCrud.controller;

import com.baseCrud.entitty.Department;
import com.baseCrud.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public Page<Department> getAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return departmentService.getAllDepartments(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable UUID id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Department> create(@RequestBody Department department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> update(@PathVariable UUID id, @RequestBody Department updatedDepartment) {
        return departmentService.updateDepartment(id, updatedDepartment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return departmentService.deleteDepartment(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
