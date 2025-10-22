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
@Table(name = "tb_ngo")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "Ngo Entity", name = "Asset")
@Tag(name = "Ngo Entity", description = "Operations related to assets")
public class Ngo extends AuditDomain {

    @Schema(description = "ID do Ngo", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "Categoria da ONG", example = "Nome da ONG")
    @NotNull(message = "NGO_REQUIRED")
    @ManyToOne
    @JoinColumn(name = "ngo_category_id", nullable = false)
    private NgoCategory ngoCategory;

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NAME_REQUIRED")
    private String name;

    @Schema(description = "Nome fantasia da ONG", example = "Nome Fantasia")
    @Column(name = "fantasy_name", unique = true, nullable = false)
    @NotNull(message = "FANTASY_NAME_REQUIRED")
    private String fantasyName;

    @Schema(description = "Descrição da OONG", example = "Organização sem fins lucrativos focada em educação ambiental")
    @NotNull(message = "DESCRIPTION_REQUIRED")
    private String description;

    @Schema(description = "CNPJ da ONG", example = "12.345.678/0001-90")
    @Column(name = "company_id", unique = true, nullable = false)
    @NotNull(message = "COMPANY_ID_REQUIRED")
    private String companyId;

    @Schema(description = "Email de contato da ONG", example = "root@localhost.com")
    @Column(name = "email", unique = true, nullable = false)
    @Email
    @NotNull(message = "EMAIL_REQUIRED")
    private String email;

    @Schema(description = "Telefone de contato da ONG", example = "(11) 91234-5678")
    @Column(name = "phone", unique = true, nullable = false)
    @NotNull(message = "PHONE_REQUIRED")
    private String phone;

    @Schema(description = "Status da ONG", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    @Column(name = "ngo_status", nullable = false)
    @NotNull(message = "NGO_STATUS_REQUIRED")
    private NgoStatus ngoStatus;
}
