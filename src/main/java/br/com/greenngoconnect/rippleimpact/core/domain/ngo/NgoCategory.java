package br.com.greenngoconnect.rippleimpact.core.domain.ngo;

import br.com.greenngoconnect.rippleimpact.core.domain.audit.AuditDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_ngo_category")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "Ngo Category Entity", name = "Asset")
@Tag(name = "Ngo Category Entity", description = "Operations related to assets")
public class NgoCategory extends AuditDomain {

    @Schema(description = "ID do Ngo", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NAME_REQUIRED")
    private String name;
}
