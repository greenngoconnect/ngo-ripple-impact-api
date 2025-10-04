package br.com.oakshield.oakshield.api.dto.request;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserRequest implements Serializable {
    private String name;
    private String email;
    private String password;
    private String role;
}
