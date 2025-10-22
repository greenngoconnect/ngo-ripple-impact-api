package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

    private final JwtServiceImpl service = new JwtServiceImpl();

    private static String generateBase64Secret() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    @Test
    void generateToken_shouldIncludeUserDetailsInClaims() {
        String secret = generateBase64Secret();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", 3600000L);
        ReflectionTestUtils.setField(service, "refreshExpiration", 86400000L);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("user@mail.com")
                .name("User Name")
                .build();

        Map<String, Object> extra = new HashMap<>();
        extra.put("customClaim", "customValue");

        String token = service.generateToken(extra, user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Decoders.BASE64.decode(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo(user.getId().toString());
        assertThat(claims.get("email", String.class)).isEqualTo(user.getEmail());
        assertThat(claims.get("name", String.class)).isEqualTo(user.getName());
        assertThat(claims.get("user_id", String.class)).isEqualTo(user.getId().toString());
        assertThat(claims.get("customClaim", String.class)).isEqualTo("customValue");
    }

    @Test
    void extractUsername_shouldReturnSubject_whenTokenIsValid() {
        String secret = generateBase64Secret();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", 3600000L);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("u@mail.com")
                .name("U")
                .build();

        String token = service.generateToken(user);

        String username = service.extractUsername(token);

        assertThat(username).isEqualTo(user.getId().toString());
    }

    @Test
    void extractUserId_shouldReturnUserId_whenTokenIsValid() {
        String secret = generateBase64Secret();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", 3600000L);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("u2@mail.com")
                .name("U2")
                .build();

        String token = service.generateToken(user);

        String userId = service.extractUserId(token);

        assertThat(userId).isEqualTo(user.getId().toString());
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenAndUserDetailsMatch() {
        String secret = generateBase64Secret();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", 3600000L);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("match@mail.com")
                .name("Match")
                .build();

        String token = service.generateToken(user);

        boolean valid = service.isTokenValid(token, user);

        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenUserIdDoesNotMatch() {
        String secret = generateBase64Secret();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", 3600000L);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("a@mail.com")
                .name("A")
                .build();

        User other = User.builder()
                .id(UUID.randomUUID())
                .email("a@mail.com")
                .name("A-other")
                .build();

        String token = service.generateToken(user);

        boolean valid = service.isTokenValid(token, other);

        assertThat(valid).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() {
        String secret = generateBase64Secret();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        // expiration negative to produce already-expired token
        ReflectionTestUtils.setField(service, "jwtExpiration", -1000L);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("exp@mail.com")
                .name("Exp")
                .build();

        String token = service.generateToken(user);

        boolean valid = service.isTokenValid(token, user);

        assertThat(valid).isFalse();
    }

    @Test
    void extractUsername_shouldThrow_whenTokenIsInvalid() {
        String secret = generateBase64Secret();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", 3600000L);

        assertThatThrownBy(() -> service.extractUsername("invalid.token.value"))
                .isInstanceOf(JwtException.class);
    }
}
