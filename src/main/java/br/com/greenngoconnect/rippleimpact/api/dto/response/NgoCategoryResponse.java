package br.com.greenngoconnect.rippleimpact.api.dto.response;

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
@Schema(description = "Ngo Category Entity", name = "Asset")
@Tag(name = "Ngo Category Entity", description = "Operations related to assets")
public class NgoCategoryResponse implements Serializable {

    @Schema(description = "ID do Ngo", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NAME_REQUIRED")
    private String name;
}
