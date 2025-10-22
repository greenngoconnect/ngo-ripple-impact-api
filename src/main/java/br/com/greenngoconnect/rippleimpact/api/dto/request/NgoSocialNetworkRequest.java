package br.com.greenngoconnect.rippleimpact.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "NgoSocialNetwork Entity", name = "Asset")
@Tag(name = "NgoSocialNetwork Entity", description = "Operations related to assets")
public class NgoSocialNetworkRequest implements Serializable {

    @Schema(description = "ID do NgoSocialNetwork", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NGO_REQUIRED")
    private UUID ngoId;

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
