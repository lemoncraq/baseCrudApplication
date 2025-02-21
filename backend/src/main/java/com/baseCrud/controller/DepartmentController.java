package com.baseCrud.controller;

import com.baseCrud.dto.DepartmentDto;
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
    public Page<DepartmentDto> getAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return departmentService.getAllDepartments(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable UUID id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentDto department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable UUID id, @RequestBody DepartmentDto updatedDepartment) {
        return departmentService.updateDepartment(id, updatedDepartment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return departmentService.deleteDepartment(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
