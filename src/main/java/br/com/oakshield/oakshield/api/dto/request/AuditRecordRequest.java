package br.com.oakshield.oakshield.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Audit Record Entity", name = "Asset")
@Tag(name = "Audit Record Entity", description = "Operations related to assets")
public class AuditRecordRequest implements Serializable {

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
