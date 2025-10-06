package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoDetailRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoDetailResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoDetailMapper;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoDetail;
import br.com.greenngoconnect.rippleimpact.core.service.NgoDetailService;
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
@RequestMapping("/v1/ngo-details")
@CrossOrigin(origins = "*")
@Tag(name = "NgoDetailController", description = "Operações com Tipos de Ativo")
@SecurityRequirement(name = "bearerAuth") // aparece o cadeado no Swagger
public class NgoDetailController {

    private final NgoDetailService service;
    private final NgoDetailMapper ngoDetailMapper;

    public NgoDetailController(NgoDetailService service, NgoDetailMapper ngoDetailMapper) {
        this.service = service;
        this.ngoDetailMapper = ngoDetailMapper;
    }

    @Operation(summary = "Create a new ngoDetail",
            description = "Creates a new ngoDetail with the provided details.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Asset type created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<NgoDetailResponse> create(@Valid @RequestBody NgoDetailRequest ngoDetailRequest) {
        NgoDetail ngoDetail = ngoDetailMapper.from(ngoDetailRequest);
        NgoDetail ngoDetailCreate = service.create(ngoDetail);
        if (ngoDetailCreate == null) {
            throw new IllegalArgumentException("Erro ao criar departamento.");
        }
        NgoDetailResponse ngoDetailResponse = ngoDetailMapper.to(ngoDetailCreate);

        URI location = RestUtils.getUri(ngoDetailResponse.getId());
        return ResponseEntity.created(location).body(ngoDetailResponse);
    }

    @Operation(summary = "Update an existing ngoDetail",
            description = "Updates the details of an existing ngoDetail by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data or ngoDetail not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<NgoDetailResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody NgoDetailRequest ngoDetailRequest) {
        NgoDetail ngoDetail = ngoDetailMapper.from(ngoDetailRequest);
        NgoDetail ngoDetailUpdate = service.update(id, ngoDetail);
        if (ngoDetailUpdate == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        NgoDetailResponse ngoDetailResponse = ngoDetailMapper.to(ngoDetailUpdate);
        return ResponseEntity.ok(ngoDetailResponse);
    }

    @Operation(summary = "Get all ngoDetails",
            description = "Retrieves a list of all ngoDetails.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of ngoDetails retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No ngoDetails found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<List<NgoDetailResponse>> findAll() {
        List<NgoDetail> ngoDetails = service.findAll();
        List<NgoDetailResponse> ngoDetailsResponse = ngoDetailMapper.map(ngoDetails);
        return ResponseEntity.ok(ngoDetailsResponse);
    }

    @Operation(summary = "Get an ngoDetail by ID",
            description = "Retrieves the details of an ngoDetail by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Asset type retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Asset type not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<NgoDetailResponse> findById(@PathVariable UUID id) {
        NgoDetail ngoDetail = service.findById(id);
        if (ngoDetail == null) {
            throw new IllegalArgumentException("Departamento não encontrado.");
        }
        NgoDetailResponse ngoDetailResponse = ngoDetailMapper.to(ngoDetail);
        return ResponseEntity.ok(ngoDetailResponse);
    }

    @Operation(summary = "Delete an ngoDetail by ID",
            description = "Deletes an ngoDetail by its ID.")
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
