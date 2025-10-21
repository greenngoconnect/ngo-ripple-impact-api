package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoCategoryRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoCategoryResponse;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoCategoryMapper;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoMapper;
import br.com.greenngoconnect.rippleimpact.config.SecurityConfig;
import br.com.greenngoconnect.rippleimpact.config.StaticResourceConfig;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoStatus;
import br.com.greenngoconnect.rippleimpact.core.service.NgoCategoryService;
import br.com.greenngoconnect.rippleimpact.core.service.NgoService;
import br.com.greenngoconnect.rippleimpact.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = NgoCategoryController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                SecurityConfig.class,             // <— fora do slice
                                JwtAuthenticationFilter.class,    // <— fora do slice
                                StaticResourceConfig.class        // se quiser manter
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
class NgoCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NgoCategoryService service;

    @MockBean
    private NgoCategoryMapper ngoMapper;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        UUID categoryId = UUID.randomUUID();

        // Request vindo do cliente
        NgoCategoryRequest req = new NgoCategoryRequest();
        req.setName("ONG Verde");

        // Domain produzido pelo mapper (entrada do service.create)
        NgoCategory mapped = NgoCategory.builder()
                .id(categoryId)
                .name("ONG Verde") // manter alinhado ao request
                .build();

        // Retorno do service
        NgoCategory saved = NgoCategory.builder()
                .id(categoryId)
                .name("ONG Verde")
                .build();

        // DTO de resposta
        NgoCategoryResponse resp = NgoCategoryResponse.builder()
                .id(categoryId)
                .name("ONG Verde")
                .build();

        given(ngoMapper.from(req)).willReturn(mapped);
        given(service.create(mapped)).willReturn(saved);
        given(ngoMapper.to(saved)).willReturn(resp);

        mockMvc.perform(post("/v1/ngo-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString(resp.getId().toString())))
                .andExpect(jsonPath("$.id").value(resp.getId().toString()))
                .andExpect(jsonPath("$.name").value("ONG Verde"));

        // Verifica o argumento REAL enviado ao service.create
        ArgumentCaptor<NgoCategory> captor = ArgumentCaptor.forClass(NgoCategory.class);
        then(service).should().create(captor.capture());
        NgoCategory sent = captor.getValue();
        assertEquals("ONG Verde", sent.getName());
        assertEquals(categoryId, sent.getId());
    }

    @Test
    void update_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        NgoCategoryRequest req = new NgoCategoryRequest();
        req.setName("NaoExiste");

        NgoCategory domain = NgoCategory.builder()
                .name(req.getName())
                .build();

        given(ngoMapper.from(req)).willReturn(domain);
        given(service.update(id, domain)).willReturn(null);

        mockMvc.perform(put("/v1/ngo-categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        UUID id = UUID.randomUUID();
        NgoCategory domain = NgoCategory.builder()
                .id(id)
                .name("ONG Verde")
                .build();

        NgoCategoryResponse dto = NgoCategoryResponse.builder()
                .id(id)
                .name("ONG Verde")
                .build();

        given(service.findAll()).willReturn(List.of(domain));
        given(ngoMapper.map(List.of(domain))).willReturn(List.of(dto));

        mockMvc.perform(get("/v1/ngo-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("ONG Verde"));
    }

    @Test
    void findAll_shouldReturn200WithEmptyList() throws Exception {
        given(service.findAll()).willReturn(List.of());
        given(ngoMapper.map(List.of())).willReturn(List.of());

        mockMvc.perform(get("/v1/ngo-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        NgoCategory domain = NgoCategory.builder()
                .id(id)
                .name("ONG Y")
                .build();

        NgoCategoryResponse dto = NgoCategoryResponse.builder()
                .id(id)
                .name("ONG Verde")
                .build();

        given(service.findById(id)).willReturn(domain);
        given(ngoMapper.to(domain)).willReturn(dto);

        mockMvc.perform(get("/v1/ngo-categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("ONG Verde"));
    }

    @Test
    void findById_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.findById(id)).willReturn(null);

        mockMvc.perform(get("/v1/ngo-categories/{id}", id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(true);

        mockMvc.perform(delete("/v1/ngo-categories/{id}", id))
                .andExpect(status().isNoContent());

        then(service).should().deleteById(id);
    }

    @Test
    void delete_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(false);

        mockMvc.perform(delete("/v1/ngo-categories/{id}", id))
                .andExpect(status().isInternalServerError());
    }
}