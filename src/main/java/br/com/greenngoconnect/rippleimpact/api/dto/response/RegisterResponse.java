package br.com.greenngoconnect.rippleimpact.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse implements Serializable {
  private UUID id;
  private String name;
  private String email;
  @JsonIgnore
  private String password;
  private String role;
  private UUID userId;
  private boolean consentAccepted;
}
