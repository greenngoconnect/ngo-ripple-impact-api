package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoCategoryRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoCategoryResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoCategoryMapper;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.service.NgoCategoryService;
import br.com.greenngoconnect.rippleimpact.utils.RestUtils;
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
@RequestMapping("/v1/ngo-categories")
@CrossOrigin(origins = "*")
@Tag(name = "NgoCategoryController", description = "Operações com Tipos de Ativo")
@SecurityRequirement(name = "bearerAuth") // aparece o cadeado no Swagger
public class NgoCategoryController {

    private final NgoCategoryService service;
    private final NgoCategoryMapper ngoCategoryMapper;

    public NgoCategoryController(NgoCategoryService service, NgoCategoryMapper ngoCategoryMapper) {
        this.service = service;
        this.ngoCategoryMapper = ngoCategoryMapper;
    }

    @Operation(summary = "Create a new ngoCategory",
            description = "Creates a new ngoCategory with the provided details.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Asset type created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<NgoCategoryResponse> create(@Valid @RequestBody NgoCategoryRequest ngoCategoryRequest) {
        NgoCategory ngoCategory = ngoCategoryMapper.from(ngoCategoryRequest);
        NgoCategory ngoCategoryCreate = service.create(ngoCategory);
        if (ngoCategoryCreate == null) {
            throw new IllegalArgumentException("Erro ao criar departamento.");
        }
        NgoCategoryResponse ngoCategoryResponse = ngoCategoryMapper.to(ngoCategoryCreate);

        URI location = RestUtils.getUri(ngoCategoryResponse.getId());
        return ResponseEntity.created(location).body(ngoCategoryResponse);
    }

    @Operation(summary = "Update an existing ngoCategory",
            description = "Updates the details of an existing ngoCategory by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data or ngoCategory not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<NgoCategoryResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody NgoCategoryRequest ngoCategoryRequest) {
        NgoCategory ngoCategory = ngoCategoryMapper.from(ngoCategoryRequest);
        NgoCategory ngoCategoryUpdate = service.update(id, ngoCategory);
        if (ngoCategoryUpdate == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        NgoCategoryResponse ngoCategoryResponse = ngoCategoryMapper.to(ngoCategoryUpdate);
        return ResponseEntity.ok(ngoCategoryResponse);
    }

    @Operation(summary = "Get all ngoCategorys",
            description = "Retrieves a list of all ngoCategorys.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of ngoCategorys retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No ngoCategorys found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<List<NgoCategoryResponse>> findAll() {
        List<NgoCategory> ngoCategorys = service.findAll();
        List<NgoCategoryResponse> ngoCategorysResponse = ngoCategoryMapper.map(ngoCategorys);
        return ResponseEntity.ok(ngoCategorysResponse);
    }

    @Operation(summary = "Get an ngoCategory by ID",
            description = "Retrieves the details of an ngoCategory by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Asset type not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<NgoCategoryResponse> findById(@PathVariable UUID id) {
        NgoCategory ngoCategory = service.findById(id);
        if (ngoCategory == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        NgoCategoryResponse ngoCategoryResponse = ngoCategoryMapper.to(ngoCategory);
        return ResponseEntity.ok(ngoCategoryResponse);
    }

    @Operation(summary = "Delete an ngoCategory by ID",
            description = "Deletes an ngoCategory by its ID.")
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
