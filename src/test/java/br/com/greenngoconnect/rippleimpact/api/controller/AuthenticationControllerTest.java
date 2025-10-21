package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.*;
import br.com.greenngoconnect.rippleimpact.api.dto.response.AuthenticationResponse;
import br.com.greenngoconnect.rippleimpact.api.dto.response.RefreshTokenResponse;
import br.com.greenngoconnect.rippleimpact.api.dto.response.RegisterResponse;
import br.com.greenngoconnect.rippleimpact.api.dto.response.UserConsent;
import br.com.greenngoconnect.rippleimpact.api.mapper.AuthenticationMapper;
import br.com.greenngoconnect.rippleimpact.config.SecurityConfig;
import br.com.greenngoconnect.rippleimpact.config.StaticResourceConfig;
import br.com.greenngoconnect.rippleimpact.core.domain.user.AuthenticationDTO;
import br.com.greenngoconnect.rippleimpact.core.domain.user.RefreshTokenDTO;
import br.com.greenngoconnect.rippleimpact.core.domain.user.Role;
import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import br.com.greenngoconnect.rippleimpact.core.service.AuthenticationService;
import br.com.greenngoconnect.rippleimpact.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthenticationController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                SecurityConfig.class,
                                JwtAuthenticationFilter.class,
                                StaticResourceConfig.class
                        }
                )
        },
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private AuthenticationService service;
    @MockBean private AuthenticationMapper authenticationMapper;

    @Test
    void registerAdmin_shouldReturn201AndLocation() throws Exception {
        UUID id = UUID.randomUUID();

        RegisterRequest req = new RegisterRequest();
        req.setName("Admin");
        req.setEmail("admin@ex.com");
        req.setPassword("pwd");
        // sem role → controller força ADMIN

        User mapped = User.builder()
                .name("Admin")
                .email("admin@ex.com")
                .password("pwd")
                .role(Role.ADMIN)
                .build();

        User saved = User.builder()
                .id(id)
                .name("Admin")
                .email("admin@ex.com")
                .role(Role.ADMIN)
                .build();

        RegisterResponse resp = RegisterResponse.builder()
                .id(id)
                .name("Admin")
                .email("admin@ex.com")
                .role(Role.ADMIN.name())
                .build();

        given(authenticationMapper.from(req)).willReturn(mapped);
        given(service.register(mapped)).willReturn(saved);
        given(authenticationMapper.to(saved)).willReturn(resp);

        mockMvc.perform(post("/v1/auth/register-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString(id.toString())))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        then(service).should().register(captor.capture());
        assertThat(captor.getValue().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void register_shouldReturn201ForNonAdminRole() throws Exception {
        UUID id = UUID.randomUUID();

        RegisterRequest req = new RegisterRequest();
        req.setName("User");
        req.setEmail("user@ex.com");
        req.setPassword("pwd");
        req.setRole(Role.USER.name()); // não-ADMIN

        User mapped = User.builder()
                .name("User")
                .email("user@ex.com")
                .password("pwd")
                .role(Role.USER)
                .build();

        User saved = User.builder()
                .id(id)
                .name("User")
                .email("user@ex.com")
                .role(Role.USER)
                .build();

        RegisterResponse resp = RegisterResponse.builder()
                .id(id)
                .name("User")
                .email("user@ex.com")
                .role(Role.USER.name())
                .build();

        given(authenticationMapper.from(req)).willReturn(mapped);
        given(service.register(mapped)).willReturn(saved);
        given(authenticationMapper.to(saved)).willReturn(resp);

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString(id.toString())))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void register_shouldReturn5xxWhenAdminRoleIsForbidden() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setName("TryAdmin");
        req.setEmail("tryadmin@ex.com");
        req.setPassword("pwd");
        req.setRole(Role.ADMIN.name()); // controller lança ResourceFoundException

        // mapper ainda pode ser chamado
        User mapped = User.builder()
                .name("TryAdmin")
                .email("tryadmin@ex.com")
                .password("pwd")
                .role(Role.ADMIN)
                .build();

        given(authenticationMapper.from(req)).willReturn(mapped);
        // service.register NÃO deve ser chamado, pois cai na exceção antes

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError()); // ajuste se houver ControllerAdvice

        then(service).should(never()).register(any());
    }

    @Test
    void resetPassword_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setEmail("a@ex.com");
        req.setPassword("newpwd");

        User updated = User.builder()
                .id(id)
                .email("a@ex.com")
                .build();

        RegisterResponse resp = RegisterResponse.builder()
                .id(id)
                .email("a@ex.com")
                .build();

        given(service.resetPassword("a@ex.com", "newpwd")).willReturn(updated);
        given(authenticationMapper.to(updated)).willReturn(resp);

        mockMvc.perform(put("/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value("a@ex.com"));
    }

    @Test
    void resetPassword_shouldReturn404WhenUserNotFound() throws Exception {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setEmail("missing@ex.com");
        req.setPassword("x");

        given(service.resetPassword("missing@ex.com", "x")).willReturn(null);

        mockMvc.perform(put("/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void forgotPassword_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("f@ex.com");

        User updated = User.builder()
                .id(id)
                .email("f@ex.com")
                .build();

        RegisterResponse resp = RegisterResponse.builder()
                .id(id)
                .email("f@ex.com")
                .build();

        given(service.forgotPassword("f@ex.com")).willReturn(updated);
        given(authenticationMapper.to(updated)).willReturn(resp);

        mockMvc.perform(put("/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value("f@ex.com"));
    }

    @Test
    void forgotPassword_shouldReturn404WhenUserNotFound() throws Exception {
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("missing@ex.com");

        given(service.forgotPassword("missing@ex.com")).willReturn(null);

        mockMvc.perform(put("/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void authenticate_shouldReturn200() throws Exception {
        AuthenticationRequest req = new AuthenticationRequest();
        req.setEmail("u@ex.com");
        req.setPassword("pwd");

        AuthenticationDTO dto = AuthenticationDTO.builder()
                .userId(UUID.randomUUID())
                .email("u@ex.com")
                .role(Role.valueOf(Role.USER.name()))
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        AuthenticationResponse resp = AuthenticationResponse.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        given(service.authenticate("u@ex.com", "pwd")).willReturn(dto);
        given(authenticationMapper.toResponse(dto)).willReturn(resp);

        mockMvc.perform(post("/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access"))
                .andExpect(jsonPath("$.refresh_token").value("refresh"));
    }

    @Test
    void refreshToken_shouldReturn200() throws Exception {
        RefreshTokenRequest req = new RefreshTokenRequest();
        req.setRefreshToken("r1");

        RefreshTokenDTO in = RefreshTokenDTO.builder().refreshToken("r1").build();
        RefreshTokenDTO out = RefreshTokenDTO.builder()
                .refreshToken("r2")
                .accessToken("a2")
                .build();
        RefreshTokenResponse resp = RefreshTokenResponse.builder()
                .accessToken("a2")
                .refreshToken("r2")
                .build();

        given(authenticationMapper.toRefreshTokenToDTO(req)).willReturn(in);
        given(service.refreshToken(in)).willReturn(out);
        given(authenticationMapper.toRefreshTokenToResponse(out)).willReturn(resp);

        mockMvc.perform(post("/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("a2"))
                .andExpect(jsonPath("$.refresh_token").value("r2"));
    }

    @Test
    void refreshToken_shouldReturn401WhenServiceReturnsNull() throws Exception {
        RefreshTokenRequest req = new RefreshTokenRequest();
        req.setRefreshToken("bad");

        RefreshTokenDTO in = RefreshTokenDTO.builder().refreshToken("bad").build();

        given(authenticationMapper.toRefreshTokenToDTO(req)).willReturn(in);
        given(service.refreshToken(in)).willReturn(null);

        mockMvc.perform(post("/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findUserById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).email("x@ex.com").build();
        RegisterResponse dto = RegisterResponse.builder().id(id).email("x@ex.com").build();

        given(service.getUser(id)).willReturn(user);
        given(authenticationMapper.to(user)).willReturn(dto);

        mockMvc.perform(get("/v1/auth/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value("x@ex.com"));
    }

    @Test
    void findUserById_shouldReturn204WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.getUser(id)).willReturn(null);

        mockMvc.perform(get("/v1/auth/user/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getConsentAccept_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        User found = User.builder().id(id).email("y@ex.com").consentAccepted(false).build();
        User updated = User.builder().id(id).email("y@ex.com").consentAccepted(true).build();

        given(service.getUser(id)).willReturn(found);
        given(service.updateUser(id, found)).willReturn(updated);

        mockMvc.perform(get("/v1/auth/user/{id}/consent/accept", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consentAccepted").value(true));
    }

    @Test
    void getConsentAccept_shouldReturn204WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.getUser(id)).willReturn(null);

        mockMvc.perform(get("/v1/auth/user/{id}/consent/accept", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void putConsentAccept_shouldReturn200True() throws Exception {
        UUID id = UUID.randomUUID();
        User found = User.builder().id(id).email("z@ex.com").consentAccepted(false).build();
        User after = User.builder().id(id).email("z@ex.com").consentAccepted(true).build();

        given(service.getUser(id)).willReturn(found);
        // controller seta consentAccepted(true) e chama updateUser
        given(service.updateUser(eq(id), any(User.class))).willReturn(after);

        mockMvc.perform(put("/v1/auth/user/{id}/consent/accept", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        then(service).should().updateUser(eq(id), captor.capture());
        assertThat(captor.getValue().isConsentAccepted()).isTrue();
    }

    @Test
    void putConsentAccept_shouldReturn204WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.getUser(id)).willReturn(null);

        mockMvc.perform(put("/v1/auth/user/{id}/consent/accept", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void logout_shouldRevokeRefreshTokenAndReturn204() throws Exception {
        // simula cookie httpOnly "refresh_token"
        Cookie refreshCookie = new Cookie("refresh_token", "r123");

        mockMvc.perform(post("/v1/auth/logout")
                        .cookie(refreshCookie))
                .andExpect(status().isNoContent());

        then(service).should().revokeRefreshToken("r123");
    }

    @Test
    void logout_shouldReadHeaderWhenNoCookie() throws Exception {
        mockMvc.perform(post("/v1/auth/logout")
                        .header("X-Refresh-Token", "r456"))
                .andExpect(status().isNoContent());

        then(service).should().revokeRefreshToken("r456");
    }
}
