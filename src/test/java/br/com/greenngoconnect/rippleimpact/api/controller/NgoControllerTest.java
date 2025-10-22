package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoMapper;
import br.com.greenngoconnect.rippleimpact.config.SecurityConfig;
import br.com.greenngoconnect.rippleimpact.config.StaticResourceConfig;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoStatus;
import br.com.greenngoconnect.rippleimpact.core.service.NgoService;
import br.com.greenngoconnect.rippleimpact.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = NgoController.class,
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
class NgoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NgoService service;
    @MockBean
    private NgoMapper ngoMapper;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        UUID categoryId = UUID.randomUUID();

        NgoRequest req = new NgoRequest();
        req.setName("ONG Verde");
        req.setEmail("root@sllc.omc");
        req.setPhone("(11) 91234-5678");
        req.setCompanyId("12.345.678/0001-90");
        req.setDescription("Organização sem fins lucrativos focada em educação ambiental");
        req.setFantasyName("ONG Verde");
        req.setNgoStatus("APPROVED"); // ou enum, conforme o tipo do seu NgoRequest
        req.setNgoCategoryId(categoryId);

        NgoCategory ngoCategory = NgoCategory.builder()
                .id(categoryId)
                .name("Educação Ambiental")
                .build();

        Ngo domain = Ngo.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .companyId(req.getCompanyId())
                .description(req.getDescription())
                .fantasyName(req.getFantasyName())
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(ngoCategory)
                .build();

        Ngo saved = Ngo.builder()
                .id(UUID.randomUUID())
                .name(domain.getName())
                .email(domain.getEmail())
                .phone(domain.getPhone())
                .companyId(domain.getCompanyId())
                .description(domain.getDescription())
                .fantasyName(domain.getFantasyName())
                .ngoStatus(domain.getNgoStatus())
                .ngoCategory(ngoCategory)
                .build();

        NgoResponse resp = NgoResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .companyId(saved.getCompanyId())
                .description(saved.getDescription())
                .fantasyName(saved.getFantasyName())
                .ngoStatus("APPROVED")
                .ngoCategoryId(categoryId)
                .build();

        given(ngoMapper.from(req)).willReturn(domain);
        given(service.create(domain)).willReturn(saved);
        given(ngoMapper.to(saved)).willReturn(resp);

        // Act + Assert
        mockMvc.perform(post("/v1/ngos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString(resp.getId().toString())))
                .andExpect(jsonPath("$.id").value(resp.getId().toString()))
                .andExpect(jsonPath("$.name").value("ONG Verde"));

        then(service).should().create(domain);
    }

    @Test
    void update_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        NgoRequest req = new NgoRequest();
        req.setName("NaoExiste");

        Ngo domain = Ngo.builder().name(req.getName()).build();

        given(ngoMapper.from(req)).willReturn(domain);
        given(service.update(id, domain)).willReturn(null);

        mockMvc.perform(put("/v1/ngos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        UUID id = UUID.randomUUID();
        Ngo domain = Ngo.builder().id(id).name("ONG Verde").build();
        NgoResponse dto = NgoResponse.builder()
                .id(id)
                .name("ONG Verde")
                .email("root@localhos.combr")
                .phone("(11) 91234-5678")
                .companyId("12.345.678/0001-90")
                .description("Organização sem fins lucrativos focada em educação ambiental")
                .fantasyName("ONG Verde")
                .ngoStatus("ACTIVE")
                .ngoCategoryId(UUID.randomUUID())
                .build();

        given(service.findAll()).willReturn(List.of(domain));
        given(ngoMapper.map(List.of(domain))).willReturn(List.of(dto));

        mockMvc.perform(get("/v1/ngos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("ONG Verde"));
    }

    @Test
    void findAll_shouldReturn200WithEmptyList() throws Exception {
        given(service.findAll()).willReturn(List.of());
        given(ngoMapper.map(List.of())).willReturn(List.of());

        mockMvc.perform(get("/v1/ngos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        Ngo domain = Ngo.builder().id(id).name("ONG Y").build();
        NgoResponse dto = NgoResponse.builder()
                .id(id)
                .name("ONG Verde")
                .email("root@localhos.combr")
                .phone("(11) 91234-5678")
                .companyId("12.345.678/0001-90")
                .description("Organização sem fins lucrativos focada em educação ambiental")
                .fantasyName("ONG Verde")
                .ngoStatus("ACTIVE")
                .ngoCategoryId(UUID.randomUUID())
                .build();

        given(service.findById(id)).willReturn(domain);
        given(ngoMapper.to(domain)).willReturn(dto);

        mockMvc.perform(get("/v1/ngos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("ONG Verde"));
    }

    @Test
    void findById_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.findById(id)).willReturn(null);

        mockMvc.perform(get("/v1/ngos/{id}", id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(true);

        mockMvc.perform(delete("/v1/ngos/{id}", id))
                .andExpect(status().isNoContent());

        then(service).should().deleteById(id);
    }

    @Test
    void delete_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(false);

        mockMvc.perform(delete("/v1/ngos/{id}", id))
                .andExpect(status().isInternalServerError());
    }
}