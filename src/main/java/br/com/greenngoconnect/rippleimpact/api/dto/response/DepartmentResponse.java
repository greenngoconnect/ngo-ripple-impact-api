package br.com.greenngoconnect.rippleimpact.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Department Entity Response", name = "Asset")
@Tag(name = "Department Entity Response", description = "Operations related to assets")
public class DepartmentResponse implements Serializable {

    @Schema(description = "ID do Employee", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nome do ativo", example = "Servidor de Banco de Dados")
    private String name;
}
