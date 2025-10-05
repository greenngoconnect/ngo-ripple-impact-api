package br.com.greenngoconnect.rippleimpact.api.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserResponse implements Serializable {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
}
