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
@Table(name = "tb_ngo_detail")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "NgoDetail Entity", name = "Asset")
@Tag(name = "NgoDetail Entity", description = "Operations related to assets")
public class NgoDetail extends AuditDomain {

    @Schema(description = "ID do NgoDetail", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NGO_REQUIRED")
    @ManyToOne
    @JoinColumn(name = "ngo_id", nullable = false)
    private Ngo ngo;

    @Schema(description = "Nome fantasia da ONG", example = "Nome Fantasia")
    @Column(name = "responsible_name", unique = true, nullable = false)
    @NotNull(message = "FANTASY_NAME_REQUIRED")
    private String responsibleName;

    @Schema(description = "Email da ONG", example = "root@localhost")
    @Column(name = "responsible_email", unique = true, nullable = false)
    @NotNull(message = "responsible_EMAIL_REQUIRED")
    private String responsibleEmail;

    @Schema(description = "Telefone da ONG", example = "(11) 91234-5678")
    @Column(name = "responsible_phone", unique = true, nullable = false)
    @NotNull(message = "responsible_PHONE_REQUIRED")
    private String responsiblePhone;

    @Schema(description = "Endereço da ONG", example = "Rua das Flores, 123, São Paulo, SP")
    @NotNull(message = "ADDRESS_REQUIRED")
    private String address;

    @Schema(description = "Missão da ONG", example = "Promover a educação ambiental e a sustentabilidade")
    private String mission;

    @Schema(description = "Visão da ONG", example = "Ser referência em educação ambiental no Brasil")
    private String vision;

    @Schema(description = "Valores da ONG", example = "Sustentabilidade, Educação, Comunidade")
    private String values;
}
