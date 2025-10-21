package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.NgoSocialNetworkRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.NgoSocialNetworkResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.NgoSocialNetworkMapper;
import br.com.greenngoconnect.rippleimpact.config.SecurityConfig;
import br.com.greenngoconnect.rippleimpact.config.StaticResourceConfig;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoSocialNetwork;
import br.com.greenngoconnect.rippleimpact.core.service.NgoSocialNetworkService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = NgoSocialNetworkController.class,
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
class NgoSocialNetworkControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private NgoSocialNetworkService service;
    @MockBean private NgoSocialNetworkMapper mapper;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        UUID networkId = UUID.randomUUID();
        UUID ngoId = UUID.randomUUID();

        // Request
        NgoSocialNetworkRequest req = new NgoSocialNetworkRequest();
        req.setNgoId(ngoId);
        req.setLinkedin("https://linkedin.com/company/ongverde");
        req.setTwitterX("@ongverde");
        req.setInstagram("@ongverde");
        req.setWebsite("https://ongverde.org");

        // Domain mapeado pelo mapper
        NgoSocialNetwork mapped = NgoSocialNetwork.builder()
                .ngo(Ngo.builder().id(ngoId).build())
                .linkedin("https://linkedin.com/company/ongverde")
                .twitterX("@ongverde")
                .instagram("@ongverde")
                .website("https://ongverde.org")
                .build();

        // Retorno do service
        NgoSocialNetwork saved = NgoSocialNetwork.builder()
                .id(networkId)
                .ngo(Ngo.builder().id(ngoId).build())
                .linkedin("https://linkedin.com/company/ongverde")
                .twitterX("@ongverde")
                .instagram("@ongverde")
                .website("https://ongverde.org")
                .build();

        // Resposta DTO
        NgoSocialNetworkResponse resp = NgoSocialNetworkResponse.builder()
                .id(networkId)
                .linkedin("https://linkedin.com/company/ongverde")
                .twitterX("@ongverde")
                .instagram("@ongverde")
                .website("https://ongverde.org")
                .build();

        given(mapper.from(req)).willReturn(mapped);
        given(service.create(mapped)).willReturn(saved);
        given(mapper.to(saved)).willReturn(resp);

        mockMvc.perform(post("/v1/ngo-social-networks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString(networkId.toString())))
                .andExpect(jsonPath("$.id").value(networkId.toString()))
                .andExpect(jsonPath("$.linkedin").value("https://linkedin.com/company/ongverde"))
                .andExpect(jsonPath("$.instagram").value("@ongverde"))
                .andExpect(jsonPath("$.website").value("https://ongverde.org"));

        // Captura o objeto enviado ao service
        ArgumentCaptor<NgoSocialNetwork> captor = ArgumentCaptor.forClass(NgoSocialNetwork.class);
        then(service).should().create(captor.capture());
        NgoSocialNetwork sent = captor.getValue();
        assertThat(sent.getNgo()).isNotNull();
        assertThat(sent.getNgo().getId()).isEqualTo(ngoId);
        assertThat(sent.getLinkedin()).isEqualTo("https://linkedin.com/company/ongverde");
        assertThat(sent.getTwitterX()).isEqualTo("@ongverde");
        assertThat(sent.getInstagram()).isEqualTo("@ongverde");
        assertThat(sent.getWebsite()).isEqualTo("https://ongverde.org");
    }

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        UUID id = UUID.randomUUID();

        NgoSocialNetwork domain = NgoSocialNetwork.builder()
                .id(id)
                .linkedin("https://linkedin.com/company/ongverde")
                .twitterX("@ongverde")
                .instagram("@ongverde")
                .website("https://ongverde.org")
                .build();

        NgoSocialNetworkResponse dto = NgoSocialNetworkResponse.builder()
                .id(id)
                .linkedin("https://linkedin.com/company/ongverde")
                .twitterX("@ongverde")
                .instagram("@ongverde")
                .website("https://ongverde.org")
                .build();

        given(service.findAll()).willReturn(List.of(domain));
        given(mapper.map(List.of(domain))).willReturn(List.of(dto));

        mockMvc.perform(get("/v1/ngo-social-networks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].linkedin").value("https://linkedin.com/company/ongverde"));
    }

    @Test
    void findById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        NgoSocialNetwork domain = NgoSocialNetwork.builder()
                .id(id)
                .linkedin("https://linkedin.com/company/ongverde")
                .twitterX("@ongverde")
                .instagram("@ongverde")
                .website("https://ongverde.org")
                .build();

        NgoSocialNetworkResponse dto = NgoSocialNetworkResponse.builder()
                .id(id)
                .linkedin("https://linkedin.com/company/ongverde")
                .twitterX("@ongverde")
                .instagram("@ongverde")
                .website("https://ongverde.org")
                .build();

        given(service.findById(id)).willReturn(domain);
        given(mapper.to(domain)).willReturn(dto);

        mockMvc.perform(get("/v1/ngo-social-networks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.linkedin").value("https://linkedin.com/company/ongverde"))
                .andExpect(jsonPath("$.website").value("https://ongverde.org"));
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(true);

        mockMvc.perform(delete("/v1/ngo-social-networks/{id}", id))
                .andExpect(status().isNoContent());

        then(service).should().deleteById(id);
    }

    @Test
    void delete_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(false);

        mockMvc.perform(delete("/v1/ngo-social-networks/{id}", id))
                .andExpect(status().isInternalServerError());
    }
}
