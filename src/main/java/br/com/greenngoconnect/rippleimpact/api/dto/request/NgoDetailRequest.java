package br.com.greenngoconnect.rippleimpact.api.dto.request;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "NgoDetail Entity", name = "Asset")
@Tag(name = "NgoDetail Entity", description = "Operations related to assets")
public class NgoDetailRequest implements Serializable {

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NGO_REQUIRED")
    private UUID ngoId;

    @Schema(description = "Nome fantasia da ONG", example = "Nome Fantasia")
    @NotNull(message = "FANTASY_NAME_REQUIRED")
    private String responsibleName;

    @Schema(description = "Email da ONG", example = "root@localhost")
    @NotNull(message = "responsible_EMAIL_REQUIRED")
    private String responsibleEmail;

    @Schema(description = "Telefone da ONG", example = "(11) 91234-5678")
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
