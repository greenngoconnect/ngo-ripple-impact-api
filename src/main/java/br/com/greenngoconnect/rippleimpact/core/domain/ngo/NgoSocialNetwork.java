package br.com.greenngoconnect.rippleimpact.core.domain.ngo;

import br.com.greenngoconnect.rippleimpact.core.domain.audit.AuditDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_ngo_social_network")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "NgoSocialNetwork Entity", name = "Asset")
@Tag(name = "NgoSocialNetwork Entity", description = "Operations related to assets")
public class NgoSocialNetwork extends AuditDomain {

    @Schema(description = "ID do NgoSocialNetwork", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NGO_REQUIRED")
    @ManyToOne
    @JoinColumn(name = "ngo_id", nullable = false)
    private Ngo ngo;

    @Schema(description = "linkedin da ONG", example = "Nome Fantasia")
    @NotNull(message = "LINKEDIN_REQUIRED")
    private String linkedin;

    @Schema(description = "TwitterX da ONG", example = "Rua das Flores, 123, São Paulo, SP")
    @Column(name = "twitter_x")
    private String twitterX;

    @Schema(description = "Instagram da ONG", example = "https://www.ongexemplo.org")
    private String instagram;

    @Schema(description = "Website da ONG", example = "https://www.ongexemplo.org")
    private String website;
}
