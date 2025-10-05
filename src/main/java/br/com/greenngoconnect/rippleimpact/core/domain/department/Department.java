package br.com.greenngoconnect.rippleimpact.core.domain.department;

import br.com.greenngoconnect.rippleimpact.core.domain.audit.AuditDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_department")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "Service Entity", name = "Asset")
@Tag(name = "Service Entity", description = "Operations related to assets")
public class Department extends AuditDomain {

    @Schema(description = "ID do Employee", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "Nome do ativo", example = "Servidor de Banco de Dados")
    private String name;
}
