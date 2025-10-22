package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoDetailRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoDetailResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoDetailMapper;
import br.com.greenngoconnect.rippleimpact.config.SecurityConfig;
import br.com.greenngoconnect.rippleimpact.config.StaticResourceConfig;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoDetail;
import br.com.greenngoconnect.rippleimpact.core.service.NgoDetailService;
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
        controllers = NgoDetailController.class,
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
class NgoDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NgoDetailService service;

    @MockBean
    private NgoDetailMapper mapper;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        UUID detailId = UUID.randomUUID();
        UUID ngoId = UUID.randomUUID();

        // Request enviado pelo cliente
        NgoDetailRequest req = new NgoDetailRequest();
        req.setNgoId(ngoId);
        req.setResponsibleName("Maria Silva");
        req.setResponsibleEmail("maria@ongverde.org");
        req.setResponsiblePhone("(11) 99999-8888");
        req.setAddress("Rua das Flores, 123");
        req.setMission("Promover educação ambiental");
        req.setVision("Ser referência em sustentabilidade");
        req.setValues("Educação, Sustentabilidade, Comunidade");

        // Domain mapeado pelo mapper
        NgoDetail mapped = NgoDetail.builder()
                .ngo(Ngo.builder().id(ngoId).build())
                .responsibleName("Maria Silva")
                .responsibleEmail("maria@ongverde.org")
                .responsiblePhone("(11) 99999-8888")
                .address("Rua das Flores, 123")
                .mission("Promover educação ambiental")
                .vision("Ser referência em sustentabilidade")
                .values("Educação, Sustentabilidade, Comunidade")
                .build();

        // Retorno do service
        NgoDetail saved = NgoDetail.builder()
                .id(detailId)
                .ngo(Ngo.builder().id(ngoId).build())
                .responsibleName("Maria Silva")
                .responsibleEmail("maria@ongverde.org")
                .responsiblePhone("(11) 99999-8888")
                .address("Rua das Flores, 123")
                .mission("Promover educação ambiental")
                .vision("Ser referência em sustentabilidade")
                .values("Educação, Sustentabilidade, Comunidade")
                .build();

        // DTO de resposta
        NgoDetailResponse resp = NgoDetailResponse.builder()
                .id(detailId)
                .responsibleName("Maria Silva")
                .responsibleEmail("maria@ongverde.org")
                .build();

        given(mapper.from(req)).willReturn(mapped);
        given(service.create(mapped)).willReturn(saved);
        given(mapper.to(saved)).willReturn(resp);

        mockMvc.perform(post("/v1/ngo-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString(detailId.toString())))
                .andExpect(jsonPath("$.id").value(detailId.toString()))
                .andExpect(jsonPath("$.responsibleName").value("Maria Silva"))
                .andExpect(jsonPath("$.responsibleEmail").value("maria@ongverde.org"));

        ArgumentCaptor<NgoDetail> captor = ArgumentCaptor.forClass(NgoDetail.class);
        then(service).should().create(captor.capture());
        NgoDetail sent = captor.getValue();
        assertEquals("Maria Silva", sent.getResponsibleName());
        assertEquals(ngoId, sent.getNgo().getId());
    }

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        UUID id = UUID.randomUUID();

        NgoDetail domain = NgoDetail.builder()
                .id(id)
                .responsibleName("João")
                .responsibleEmail("joao@ong.org")
                .build();

        NgoDetailResponse dto = NgoDetailResponse.builder()
                .id(id)
                .responsibleName("João")
                .responsibleEmail("joao@ong.org")
                .build();

        given(service.findAll()).willReturn(List.of(domain));
        given(mapper.map(List.of(domain))).willReturn(List.of(dto));

        mockMvc.perform(get("/v1/ngo-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].responsibleName").value("João"));
    }

    @Test
    void findById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        NgoDetail domain = NgoDetail.builder()
                .id(id)
                .responsibleName("Maria")
                .responsibleEmail("maria@ongverde.org")
                .build();

        NgoDetailResponse dto = NgoDetailResponse.builder()
                .id(id)
                .responsibleName("Maria")
                .responsibleEmail("maria@ongverde.org")
                .build();

        given(service.findById(id)).willReturn(domain);
        given(mapper.to(domain)).willReturn(dto);

        mockMvc.perform(get("/v1/ngo-details/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.responsibleName").value("Maria"));
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(true);

        mockMvc.perform(delete("/v1/ngo-details/{id}", id))
                .andExpect(status().isNoContent());

        then(service).should().deleteById(id);
    }

    @Test
    void delete_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(false);

        mockMvc.perform(delete("/v1/ngo-details/{id}", id))
                .andExpect(status().isInternalServerError());
    }
}
