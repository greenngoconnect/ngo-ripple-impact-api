package br.com.greenngoconnect.rippleimpact.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "Ngo Category Entity", name = "Asset")
@Tag(name = "Ngo  Category Entity", description = "Operations related to assets")
public class NgoCategoryRequest implements Serializable {

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NAME_REQUIRED")
    private String name;
}
