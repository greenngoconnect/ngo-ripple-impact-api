package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.request.DepartmentRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.DepartmentResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.DepartmentMapper;
import br.com.greenngoconnect.rippleimpact.config.SecurityConfig;
import br.com.greenngoconnect.rippleimpact.config.StaticResourceConfig;
import br.com.greenngoconnect.rippleimpact.core.domain.department.Department;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.service.DepartmentService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = DepartmentController.class,
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
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService service;

    @MockBean
    private DepartmentMapper departmentMapper;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        UUID departmentId = UUID.randomUUID();
        UUID ngoId = UUID.randomUUID();

        // Request do cliente
        DepartmentRequest req = new DepartmentRequest();
        req.setName("Departamento Verde");
        // supondo que o DTO tenha este campo:
        req.setMgoId(ngoId);

        // Domain mapeado pelo mapper (entrada do service.create)
        Department mapped = Department.builder()
                .name("Departamento Verde")
                .ngo(Ngo.builder().id(ngoId).build()) // ManyToOne obrigatório
                .build();

        // Retorno do service
        Department saved = Department.builder()
                .id(departmentId)
                .name("Departamento Verde")
                .ngo(Ngo.builder().id(ngoId).build())
                .build();

        // DTO de resposta
        DepartmentResponse resp = DepartmentResponse.builder()
                .id(departmentId)
                .name("Departamento Verde")
                .build();

        given(departmentMapper.from(req)).willReturn(mapped);
        given(service.create(mapped)).willReturn(saved);
        given(departmentMapper.to(saved)).willReturn(resp);

        mockMvc.perform(post("/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString(resp.getId().toString())))
                .andExpect(jsonPath("$.id").value(resp.getId().toString()))
                .andExpect(jsonPath("$.name").value("Departamento Verde"));

        // Captura o argumento real enviado ao service.create
        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
        then(service).should().create(captor.capture());
        Department sent = captor.getValue();
        assertEquals("Departamento Verde", sent.getName());
        assertEquals(ngoId, sent.getNgo().getId());
    }

    @Test
    void update_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ngoId = UUID.randomUUID();

        DepartmentRequest req = new DepartmentRequest();
        req.setName("NaoExiste");
        req.setMgoId(ngoId);

        Department domain = Department.builder()
                .name(req.getName())
                .ngo(Ngo.builder().id(ngoId).build())
                .build();

        given(departmentMapper.from(req)).willReturn(domain);
        given(service.update(id, domain)).willReturn(null);

        mockMvc.perform(put("/v1/departments/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ngoId = UUID.randomUUID();

        Department domain = Department.builder()
                .id(id)
                .name("Departamento Verde")
                .ngo(Ngo.builder().id(ngoId).build())
                .build();

        DepartmentResponse dto = DepartmentResponse.builder()
                .id(id)
                .name("Departamento Verde")
                .build();

        given(service.findAll()).willReturn(List.of(domain));
        given(departmentMapper.map(List.of(domain))).willReturn(List.of(dto));

        mockMvc.perform(get("/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("Departamento Verde"));
    }

    @Test
    void findAll_shouldReturn200WithEmptyList() throws Exception {
        given(service.findAll()).willReturn(List.of());
        given(departmentMapper.map(List.of())).willReturn(List.of());

        mockMvc.perform(get("/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ngoId = UUID.randomUUID();

        Department domain = Department.builder()
                .id(id)
                .name("Departamento Y")
                .ngo(Ngo.builder().id(ngoId).build())
                .build();

        DepartmentResponse dto = DepartmentResponse.builder()
                .id(id)
                .name("Departamento Verde")
                .build();

        given(service.findById(id)).willReturn(domain);
        given(departmentMapper.to(domain)).willReturn(dto);

        mockMvc.perform(get("/v1/departments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Departamento Verde"));
    }

    @Test
    void findById_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.findById(id)).willReturn(null);

        mockMvc.perform(get("/v1/departments/{id}", id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(true);

        mockMvc.perform(delete("/v1/departments/{id}", id))
                .andExpect(status().isNoContent());

        then(service).should().deleteById(id);
    }

    @Test
    void delete_shouldReturnServerErrorWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(service.existsById(id)).willReturn(false);

        mockMvc.perform(delete("/v1/departments/{id}", id))
                .andExpect(status().isInternalServerError());
    }
}
