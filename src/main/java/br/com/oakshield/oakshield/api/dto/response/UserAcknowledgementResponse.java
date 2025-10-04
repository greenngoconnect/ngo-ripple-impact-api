package br.com.oakshield.oakshield.api.dto.response;

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
@Schema(description = "User Acknowledgement Entity Response", name = "Asset")
@Tag(name = "User Acknowledgement Entity Response", description = "Operations related to assets")
public class UserAcknowledgementResponse implements Serializable {

    @Schema(description = "ID da confirmação do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "ID do usuário que reconheceu a política", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "ID da política reconhecida pelo usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID policyId;

    @Schema(description = "Data em que o usuário reconheceu a política", example = "2023-10-01")
    private LocalDateTime acknowledgedAt;

}