package br.com.oakshield.oakshield.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Department Entity", name = "Asset")
@Tag(name = "Department Entity", description = "Operations related to assets")
public class DepartmentRequest implements Serializable {

    @Schema(description = "Nome do ativo", example = "Servidor de Banco de Dados")
    private String name;
}
