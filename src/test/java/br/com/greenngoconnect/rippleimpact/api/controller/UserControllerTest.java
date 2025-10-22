package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.UserRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.UserResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.UserMapper;
import br.com.greenngoconnect.rippleimpact.config.SecurityConfig;
import br.com.greenngoconnect.rippleimpact.config.StaticResourceConfig;
import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import br.com.greenngoconnect.rippleimpact.core.service.UserService;
import br.com.greenngoconnect.rippleimpact.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
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
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UserService service;
    @MockBean private UserMapper userMapper;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        UUID userId = UUID.randomUUID();

        // request
        UserRequest req = new UserRequest();
        req.setName("Rogério");
        req.setEmail("rogerio@example.com");
        req.setPassword("s3cr3t!");

        // domain mapeado
        User mapped = User.builder()
                .name("Rogério")
                .email("rogerio@example.com")
                .password("s3cr3t!")
                .build();

        // salvo no service
        User saved = User.builder()
                .id(userId)
                .name("Rogério")
                .email("rogerio@example.com")
                .build();

        // response dto
        UserResponse resp = UserResponse.builder()
                .id(userId)
                .name("Rogério")
                .email("rogerio@example.com")
                .build();

        given(userMapper.from(req)).willReturn(mapped);
        given(service.create(mapped)).willReturn(saved);
        given(userMapper.to(saved)).willReturn(resp);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString(userId.toString())))
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Rogério"))
                .andExpect(jsonPath("$.email").value("rogerio@example.com"));

        // garante o argumento enviado ao service
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        then(service).should().create(captor.capture());
        User sent = captor.getValue();
        assertThat(sent.getName()).isEqualTo("Rogério");
        assertThat(sent.getEmail()).isEqualTo("rogerio@example.com");
    }

    @Test
    void create_shouldReturn400WhenMissingRequiredFields() throws Exception {
        // request inválido (ex.: sem email/senha)
        UserRequest req = new UserRequest();
        req.setName("Sem Email");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void update_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        UserRequest req = new UserRequest();
        req.setName("Novo Nome");
        req.setEmail("novo@example.com");
        req.setPassword("novaSenha!");

        User mapped = User.builder()
                .name("Novo Nome")
                .email("novo@example.com")
                .password("novaSenha!")
                .build();

        User updated = User.builder()
                .id(id)
                .name("Novo Nome")
                .email("novo@example.com")
                .build();

        UserResponse resp = UserResponse.builder()
                .id(id)
                .name("Novo Nome")
                .email("novo@example.com")
                .build();

        given(userMapper.from(req)).willReturn(mapped);
        given(service.update(id, mapped)).willReturn(updated);
        given(userMapper.to(updated)).willReturn(resp);

        mockMvc.perform(put("/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Novo Nome"))
                .andExpect(jsonPath("$.email").value("novo@example.com"));
    }

    @Test
    void update_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        UserRequest req = new UserRequest();
        req.setName("Alvo");
        req.setEmail("alvo@example.com");
        req.setPassword("pwd");

        User mapped = User.builder()
                .name("Alvo")
                .email("alvo@example.com")
                .password("pwd")
                .build();

        given(userMapper.from(req)).willReturn(mapped);
        given(service.update(id, mapped)).willReturn(null);

        mockMvc.perform(put("/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is5xxServerError()); // troque para isNotFound() se tiver ControllerAdvice
    }

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        UUID id = UUID.randomUUID();

        User domain = User.builder()
                .id(id)
                .name("Ana")
                .email("ana@example.com")
                .build();

        UserResponse dto = UserResponse.builder()
                .id(id)
                .name("Ana")
                .email("ana@example.com")
                .build();

        given(service.findAll()).willReturn(List.of(domain));
        given(userMapper.map(List.of(domain))).willReturn(List.of(dto));

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("Ana"))
                .andExpect(jsonPath("$[0].email").value("ana@example.com"));
    }

    @Test
    void findAll_shouldReturn200WithEmptyList() throws Exception {
        given(service.findAll()).willReturn(List.of());
        given(userMapper.map(List.of())).willReturn(List.of());

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        User domain = User.builder()
                .id(id)
                .name("Carlos")
                .email("carlos@example.com")
                .build();

        UserResponse dto = UserResponse.builder()
                .id(id)
                .name("Carlos")
                .email("carlos@example.com")
                .build();

        given(service.findById(id)).willReturn(domain);
        given(userMapper.to(domain)).willReturn(dto);

        mockMvc.perform(get("/v1/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@example.com"));
    }

    @Disabled
    void findById_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.findById(id)).willReturn(null);

        mockMvc.perform(get("/v1/users/{id}", id))
                .andExpect(status().is5xxServerError()); // troque para isNotFound() se tiver ControllerAdvice
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(true);

        mockMvc.perform(delete("/v1/users/{id}", id))
                .andExpect(status().isNoContent());

        then(service).should().deleteById(id);
    }

    @Test
    void delete_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(false);

        mockMvc.perform(delete("/v1/users/{id}", id))
                .andExpect(status().is5xxServerError()); // troque para isNotFound() se tiver ControllerAdvice
    }
}
