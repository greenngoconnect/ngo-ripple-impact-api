package br.com.greenngoconnect.rippleimpact.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
@Schema(description = "Driver Request")
public class ForgotPasswordRequest implements Serializable {
    @NotEmpty(message = "EMAIL_REQUIRED")
    @Email(message = "NOT_VALID_EMAIL")
    private String email;
}
