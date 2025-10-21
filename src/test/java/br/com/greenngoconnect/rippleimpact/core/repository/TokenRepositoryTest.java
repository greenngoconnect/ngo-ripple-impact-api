package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.user.Token;
import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByToken_shouldReturnToken_whenExists() {
        User user = userRepository.save(User.builder()
                .name("Evaldo")
                .email("evaldo@exemplo.com")
                .password("{noop}123")
                // .role(Role.ADMIN)
                .build());

        Token t = tokenRepository.save(Token.builder()
                .token("abc123")
                .expired(false)
                .revoked(false)
                .user(user)
                .build());

        var found = tokenRepository.findByToken("abc123");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(t.getId());
    }

    @Test
    void findAllValidTokenByUser_shouldReturnNonExpiredOrNonRevoked() {
        User user = userRepository.save(User.builder()
                .name("Paula")
                .email("paula@exemplo.com")
                .password("{noop}pwd")
                .build());

        // válidos
        tokenRepository.save(Token.builder().token("t1").expired(false).revoked(false).user(user).build());
        tokenRepository.save(Token.builder().token("t2").expired(false).revoked(true).user(user).build());
        tokenRepository.save(Token.builder().token("t3").expired(true).revoked(false).user(user).build());

        // inválido (ambos true)
        tokenRepository.save(Token.builder().token("t4").expired(true).revoked(true).user(user).build());

        List<Token> valid = tokenRepository.findAllValidTokenByUser(user.getId());

        // Pela query: (expired = false OR revoked = false)
        // Portanto t1, t2 e t3 são considerados "válidos" (qualquer um false).
        assertThat(valid).extracting(Token::getToken)
                .containsExactlyInAnyOrder("t1", "t2", "t3")
                .doesNotContain("t4");
    }
}
