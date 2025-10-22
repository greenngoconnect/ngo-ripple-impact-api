package br.com.greenngoconnect.rippleimpact.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "Ngo Entity", name = "Asset")
@Tag(name = "Ngo Entity", description = "Operations related to assets")
public class NgoResponse implements Serializable {

    @Schema(description = "ID do Ngo", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Categoria da ONG", example = "Nome da ONG")
    @NotNull(message = "NGO_REQUIRED")
    private UUID ngoCategoryId;

    @Schema(description = "Nome da ONG", example = "Nome da ONG")
    @NotNull(message = "NAME_REQUIRED")
    private String name;

    @Schema(description = "Nome fantasia da ONG", example = "Nome Fantasia")
    @NotNull(message = "FANTASY_NAME_REQUIRED")
    private String fantasyName;

    @Schema(description = "Descrição da OONG", example = "Organização sem fins lucrativos focada em educação ambiental")
    @NotNull(message = "DESCRIPTION_REQUIRED")
    private String description;

    @Schema(description = "CNPJ da ONG", example = "12.345.678/0001-90")
    @NotNull(message = "COMPANY_ID_REQUIRED")
    private String companyId;

    @Schema(description = "Email de contato da ONG", example = "root@localhost.com")
    @Email
    @NotNull(message = "EMAIL_REQUIRED")
    private String email;

    @Schema(description = "Telefone de contato da ONG", example = "(11) 91234-5678")
    @NotNull(message = "PHONE_REQUIRED")
    private String phone;

    @Schema(description = "Status da ONG", example = "ACTIVE")
    @NotNull(message = "NGO_STATUS_REQUIRED")
    private String ngoStatus;
}
