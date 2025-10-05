package br.com.greenngoconnect.rippleimpact.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "User Acknowledgement Entity", name = "Asset")
@Tag(name = "User Acknowledgement Entity", description = "Operations related to assets")
public class UserAcknowledgementRequest implements Serializable {

    @Schema(description = "ID do usuário que reconheceu a política", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Data em que o usuário reconheceu a política", example = "2023-10-01")
    private LocalDateTime acknowledgedAt;

}