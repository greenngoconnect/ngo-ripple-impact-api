// src/test/java/br/com/greenngoconnect/rippleimpact/core/service/impl/AuthenticationServiceImplTest.java
package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.commons.DateConstants;
import br.com.greenngoconnect.rippleimpact.core.domain.user.*;
import br.com.greenngoconnect.rippleimpact.core.repository.TokenRepository;
import br.com.greenngoconnect.rippleimpact.core.service.JwtService;
import br.com.greenngoconnect.rippleimpact.core.service.UserService;
import br.com.greenngoconnect.rippleimpact.exception.EmailFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @Test
    void register_shouldSaveUser_whenEmailIsNotInUse() throws Exception {
        User user = User.builder()
                .email("new@mail.com")
                .password("plain")
                .name("New")
                .build();
        User saved = User.builder()
                .id(UUID.randomUUID())
                .email(user.getEmail())
                .password("encoded")
                .name(user.getName())
                .build();

        given(userService.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode("plain")).willReturn("encoded");
        given(userService.create(user)).willReturn(saved);

        User result = service.register(user);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("new@mail.com");
        then(userService).should().findByEmail(user.getEmail());
        then(userService).should().create(user);
    }

    @Test
    void register_shouldThrow_whenEmailAlreadyInUse() {
        User user = User.builder()
                .email("exist@mail.com")
                .password("p")
                .build();

        given(userService.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThatThrownBy(() -> service.register(user))
                .isInstanceOf(EmailFoundException.class)
                .hasMessageContaining("Email já cadastrado");

        then(userService).should().findByEmail(user.getEmail());
    }

    @Test
    void forgotPassword_shouldUpdatePassword_whenEmailExists() throws Exception {
        String email = "forgot@mail.com";
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("old")
                .build();
        given(userService.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(DateConstants.RAW_PASSWORD)).willReturn("rawEncoded");
        given(userService.create(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User result = service.forgotPassword(email);

        assertThat(result).isNotNull();
        assertThat(result.getPassword()).isEqualTo("rawEncoded");
        then(userService).should().findByEmail(email);
        then(userService).should().create(user);
    }

    @Test
    void forgotPassword_shouldReturnNull_whenEmailNotFound() throws Exception {
        String email = "notfound@mail.com";
        given(userService.findByEmail(email)).willReturn(Optional.empty());

        User result = service.forgotPassword(email);

        assertThat(result).isNull();
        then(userService).should().findByEmail(email);
    }

    @Test
    void resetPassword_shouldThrow_whenUserNotFound() throws Exception {
        String email = "no@mail.com";
        given(userService.findByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.resetPassword(email, "new"))
                .isInstanceOf(ResourceFoundException.class)
                .hasMessageContaining("Email já cadastrado");

        then(userService).should().findByEmail(email);
    }

    @Test
    void resetPassword_shouldUpdatePassword_whenUserFound() throws Exception {
        String email = "yes@mail.com";
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("current")
                .build();
        given(userService.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.encode("current")).willReturn("encodedCurrent");
        given(userService.create(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User result = service.resetPassword(email, "ignoredNewPassword");

        assertThat(result).isNotNull();
        assertThat(result.getPassword()).isEqualTo("encodedCurrent");
        then(userService).should().findByEmail(email);
        then(userService).should().create(user);
    }

    @Test
    void authenticate_shouldReturnAuthenticationDTO_whenCredentialsAreValid() {
        String email = "auth@mail.com";
        String password = "pwd";
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .name("Name")
                .role(Role.USER)
                .build();

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
                .willReturn(mock(Authentication.class));
        given(userService.findByEmail(email)).willReturn(Optional.of(user));
        given(jwtService.generateToken(user)).willReturn("jwt.token");
        given(jwtService.generateRefreshToken(user)).willReturn("refresh.token");
        given(tokenRepository.findAllValidTokenByUser(user.getId())).willReturn(Collections.emptyList());
        given(tokenRepository.save(any(Token.class))).willAnswer(inv -> inv.getArgument(0));

        AuthenticationDTO dto = service.authenticate(email, password);

        assertThat(dto).isNotNull();
        assertThat(dto.getAccessToken()).isEqualTo("jwt.token");
        assertThat(dto.getRefreshToken()).isEqualTo("refresh.token");
        assertThat(dto.getEmail()).isEqualTo(email);
        then(authenticationManager).should().authenticate(new UsernamePasswordAuthenticationToken(email, password));
        then(userService).should().findByEmail(email);
        then(jwtService).should().generateToken(user);
        then(jwtService).should().generateRefreshToken(user);
        then(tokenRepository).should().save(any(Token.class));
    }

    @Test
    void authenticate_shouldThrow_whenUserNotFound() {
        String email = "nouser@mail.com";
        String password = "pwd";
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
                .willReturn(mock(Authentication.class));
        given(userService.findByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.authenticate(email, password))
                .isInstanceOf(RuntimeException.class);

        then(userService).should().findByEmail(email);
    }

    @Test
    void revokeRefreshToken_shouldRevokeAndSave_whenTokenExists() {
        String tokenStr = "rtoken";
        Token token = Token.builder()
                .token(tokenStr)
                .revoked(false)
                .expired(false)
                .tokenType(TokenType.BEARER)
                .build();

        given(tokenRepository.findByToken(tokenStr)).willReturn(Optional.of(token));
        given(tokenRepository.save(token)).willReturn(token);

        service.revokeRefreshToken(tokenStr);

        assertThat(token.isRevoked()).isTrue();
        then(tokenRepository).should().save(token);
    }

    @Test
    void revokeRefreshToken_shouldDoNothing_whenTokenNotFound() {
        String tokenStr = "none";
        given(tokenRepository.findByToken(tokenStr)).willReturn(Optional.empty());

        service.revokeRefreshToken(tokenStr);

        then(tokenRepository).should().findByToken(tokenStr);
        then(tokenRepository).should(times(0)).save(any());
    }

    @Test
    void refreshToken_shouldReturnNewAccessToken_whenRefreshTokenIsValid() {
        String refreshToken = "refresh";
        String email = "r@mail.com";
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .build();

        given(jwtService.extractUsername(refreshToken)).willReturn(email);
        given(userService.findByEmail(email)).willReturn(Optional.of(user));
        given(jwtService.isTokenValid(refreshToken, user)).willReturn(true);
        given(jwtService.generateToken(user)).willReturn("new.access");
        given(tokenRepository.findAllValidTokenByUser(user.getId())).willReturn(Collections.emptyList());
        given(tokenRepository.save(any(Token.class))).willAnswer(inv -> inv.getArgument(0));

        RefreshTokenDTO dto = RefreshTokenDTO.builder().refreshToken(refreshToken).build();

        RefreshTokenDTO result = service.refreshToken(dto);

        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("new.access");
        assertThat(result.getRefreshToken()).isEqualTo(refreshToken);
        then(jwtService).should().extractUsername(refreshToken);
        then(jwtService).should().isTokenValid(refreshToken, user);
        then(jwtService).should().generateToken(user);
        then(tokenRepository).should().save(any(Token.class));
    }

    @Test
    void refreshToken_shouldReturnNull_whenExtractedUsernameIsNull() {
        String refreshToken = "invalid";
        given(jwtService.extractUsername(refreshToken)).willReturn(null);

        RefreshTokenDTO dto = RefreshTokenDTO.builder().refreshToken(refreshToken).build();

        RefreshTokenDTO result = service.refreshToken(dto);

        assertThat(result).isNull();
        then(jwtService).should().extractUsername(refreshToken);
    }

    @Test
    void getUser_shouldReturnUserFromUserService() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).email("u@mail.com").build();
        given(userService.findById(id)).willReturn(user);

        User result = service.getUser(id);

        assertThat(result).isEqualTo(user);
        then(userService).should().findById(id);
    }

    @Test
    void updateUser_shouldReturnNull_whenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        User payload = User.builder().name("X").build();
        given(userService.existsById(id)).willReturn(false);

        User result = service.updateUser(id, payload);

        assertThat(result).isNull();
        then(userService).should().existsById(id);
    }

    @Test
    void updateUser_shouldUpdateAndReturn_whenUserExists() {
        UUID id = UUID.randomUUID();
        User payload = User.builder().name("ToUpdate").build();
        User updated = User.builder().id(id).name("ToUpdate").build();

        given(userService.existsById(id)).willReturn(true);
        given(userService.update(eq(id), eq(payload))).willReturn(updated);

        User result = service.updateUser(id, payload);

        assertThat(result).isEqualTo(updated);
        then(userService).should().existsById(id);
        then(userService).should().update(id, payload);
    }
}
