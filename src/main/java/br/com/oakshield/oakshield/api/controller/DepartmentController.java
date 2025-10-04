package br.com.oakshield.oakshield.api.controller;

import br.com.oakshield.oakshield.api.dto.request.DepartmentRequest;
import br.com.oakshield.oakshield.api.mapper.DepartmentMapper;
import br.com.oakshield.oakshield.core.domain.department.Department;
import br.com.oakshield.oakshield.core.service.DepartmentService;
import br.com.oakshield.oakshield.utils.RestUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/departments")
@CrossOrigin(origins = "*")
@Tag(name = "DepartmentController", description = "Operações com Tipos de Ativo")
@SecurityRequirement(name = "bearerAuth") // aparece o cadeado no Swagger
public class DepartmentController {

    private final DepartmentService service;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService service, DepartmentMapper departmentMapper) {
        this.service = service;
        this.departmentMapper = departmentMapper;
    }

    @Operation(summary = "Create a new department",
            description = "Creates a new department with the provided details.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Asset type created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest departmentRequest) {
        Department department = departmentMapper.from(departmentRequest);
        Department departmentCreate = service.create(department);
        if (departmentCreate == null) {
            throw new IllegalArgumentException("Erro ao criar departamento.");
        }
        DepartmentResponse departmentResponse = departmentMapper.to(departmentCreate);

        URI location = RestUtils.getUri(departmentResponse.getId());
        return ResponseEntity.created(location).body(departmentResponse);
    }

    @Operation(summary = "Update an existing department",
            description = "Updates the details of an existing department by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data or department not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody DepartmentRequest departmentRequest) {
        Department department = departmentMapper.from(departmentRequest);
        Department departmentUpdate = service.update(id, department);
        if (departmentUpdate == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        DepartmentResponse departmentResponse = departmentMapper.to(departmentUpdate);
        return ResponseEntity.ok(departmentResponse);
    }

    @Operation(summary = "Get all departments",
            description = "Retrieves a list of all departments.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of departments retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No departments found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> findAll() {
        List<Department> departments = service.findAll();
        List<DepartmentResponse> departmentsResponse = departmentMapper.map(departments);
        return ResponseEntity.ok(departmentsResponse);
    }

    @Operation(summary = "Get an department by ID",
            description = "Retrieves the details of an department by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Asset type not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> findById(@PathVariable UUID id) {
        Department department = service.findById(id);
        if (department == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        DepartmentResponse departmentResponse = departmentMapper.to(department);
        return ResponseEntity.ok(departmentResponse);
    }

    @Operation(summary = "Delete an department by ID",
            description = "Deletes an department by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Asset type deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Asset type not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!service.existsById(id)) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
