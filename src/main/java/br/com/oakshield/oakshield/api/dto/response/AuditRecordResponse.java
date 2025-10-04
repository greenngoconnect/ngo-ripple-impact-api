package br.com.oakshield.oakshield.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Audit Record Entity Response", name = "Asset")
@Tag(name = "Audit Record Entity Response", description = "Operations related to assets")
public class AuditRecordResponse implements Serializable {

    @Schema(description = "ID do registro de auditoria", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Cláusula auditada", example = "Cláusula 1: Proteção de Dados")
    private String clause;

    @Schema(description = "Evidência coletada", example = "Relatório de conformidade")
    private String evidence;

    @Schema(description = "Data da auditoria", example = "2023-10-01")
    private LocalDate date;

    @Schema(description = "Auditor responsável", example = "João da Silva")
    private String auditor;

    private String auditStatus;

    // TODO - criar upload de arquivos para evidências - media like maos encantadas
}
