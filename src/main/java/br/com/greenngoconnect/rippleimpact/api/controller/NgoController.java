package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoMapper;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.service.NgoService;
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
@RequestMapping("/v1/ngos")
@CrossOrigin(origins = "*")
@Tag(name = "NgoController", description = "Operações com Tipos de Ativo")
@SecurityRequirement(name = "bearerAuth") // aparece o cadeado no Swagger
public class NgoController {

    private final NgoService service;
    private final NgoMapper ngoMapper;

    public NgoController(NgoService service, NgoMapper ngoMapper) {
        this.service = service;
        this.ngoMapper = ngoMapper;
    }

    @Operation(summary = "Create a new ngo",
            description = "Creates a new ngo with the provided details.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Asset type created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<NgoResponse> create(@Valid @RequestBody NgoRequest ngoRequest) {
        Ngo ngo = ngoMapper.from(ngoRequest);
        Ngo ngoCreate = service.create(ngo);
        if (ngoCreate == null) {
            throw new IllegalArgumentException("Erro ao criar departamento.");
        }
        NgoResponse ngoResponse = ngoMapper.to(ngoCreate);

        URI location = RestUtils.getUri(ngoResponse.getId());
        return ResponseEntity.created(location).body(ngoResponse);
    }

    @Operation(summary = "Update an existing ngo",
            description = "Updates the details of an existing ngo by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data or ngo not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<NgoResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody NgoRequest ngoRequest) {
        Ngo ngo = ngoMapper.from(ngoRequest);
        Ngo ngoUpdate = service.update(id, ngo);
        if (ngoUpdate == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        NgoResponse ngoResponse = ngoMapper.to(ngoUpdate);
        return ResponseEntity.ok(ngoResponse);
    }

    @Operation(summary = "Get all ngos",
            description = "Retrieves a list of all ngos.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of ngos retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No ngos found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<List<NgoResponse>> findAll() {
        List<Ngo> ngos = service.findAll();
        List<NgoResponse> ngosResponse = ngoMapper.map(ngos);
        return ResponseEntity.ok(ngosResponse);
    }

    @Operation(summary = "Get an ngo by ID",
            description = "Retrieves the details of an ngo by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Asset type not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<NgoResponse> findById(@PathVariable UUID id) {
        Ngo ngo = service.findById(id);
        if (ngo == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        NgoResponse ngoResponse = ngoMapper.to(ngo);
        return ResponseEntity.ok(ngoResponse);
    }

    @Operation(summary = "Delete an ngo by ID",
            description = "Deletes an ngo by its ID.")
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
