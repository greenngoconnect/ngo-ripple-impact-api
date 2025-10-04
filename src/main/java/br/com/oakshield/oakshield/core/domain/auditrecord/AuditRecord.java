package br.com.oakshield.oakshield.core.domain.auditrecord;

import br.com.oakshield.oakshield.core.domain.audit.AuditDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_audit_record")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "Audit Record Entity", name = "Asset")
@Tag(name = "Audit Record Entity", description = "Operations related to assets")
public class AuditRecord extends AuditDomain {

    @Schema(description = "ID do registro de auditoria", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "Cláusula auditada", example = "Cláusula 1: Proteção de Dados")
    private String clause;

    @Schema(description = "Evidência coletada", example = "Relatório de conformidade")
    private String evidence;

    @Schema(description = "Data da auditoria", example = "2023-10-01")
    private LocalDate date;

    @Schema(description = "Auditor responsável", example = "João da Silva")
    private String auditor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "audit_status")
    private AuditStatus auditStatus;

    // TODO - criar upload de arquivos para evidências - media like maos encantadas
}
