package br.com.greenngoconnect.rippleimpact.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class AuthenticationResponse implements Serializable {

  @JsonProperty("name")
  private String name;

  @JsonProperty("email")
  private String email;

  @JsonIgnore
  @JsonProperty("password")
  private String password;

  @JsonProperty("role")
  @Enumerated(EnumType.STRING)
  private String role;

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("user_id")
  private UUID userId;
}
