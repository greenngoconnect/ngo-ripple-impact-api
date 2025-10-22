// src/test/java/br/com/greenngoconnect/rippleimpact/core/service/impl/NgoDetailServiceImplTest.java
package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoDetail;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoDetailRepository;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class NgoDetailServiceImplTest {

    @Mock
    private NgoDetailRepository repository;

    @InjectMocks
    private NgoDetailServiceImpl service;

    @Test
    void create_shouldPersistNgoDetail_whenValid() {
        Ngo ngo = Ngo.builder().id(UUID.randomUUID()).build();
        NgoDetail detail = NgoDetail.builder()
                .ngo(ngo)
                .responsibleName("João Silva")
                .responsibleEmail("joao@mail.com")
                .responsiblePhone("(11) 90000-0000")
                .address("Rua A, 1")
                .build();

        given(repository.save(detail)).willReturn(detail);

        NgoDetail created = service.create(detail);

        assertThat(created).isNotNull();
        assertThat(created.getResponsibleName()).isEqualTo("João Silva");
        then(repository).should().save(detail);
    }

    @Test
    void findAll_shouldReturnAllNgoDetails_whenDataExists() {
        Ngo ngo = Ngo.builder().id(UUID.randomUUID()).build();
        NgoDetail d1 = NgoDetail.builder()
                .id(UUID.randomUUID())
                .ngo(ngo)
                .address("End 1")
                .responsibleName("A")
                .responsibleEmail("a@mail.com")
                .responsiblePhone("(11) 90000-0001")
                .build();
        NgoDetail d2 = NgoDetail.builder()
                .id(UUID.randomUUID())
                .ngo(ngo)
                .address("End 2")
                .responsibleName("B")
                .responsibleEmail("b@mail.com")
                .responsiblePhone("(11) 90000-0002")
                .build();

        given(repository.findAll()).willReturn(List.of(d1, d2));

        var all = service.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(NgoDetail::getAddress).containsExactlyInAnyOrder("End 1", "End 2");
        then(repository).should().findAll();
    }

    @Test
    void update_shouldUpdateNgoDetail_whenExists() {
        UUID id = UUID.randomUUID();
        Ngo ngo = Ngo.builder().id(UUID.randomUUID()).build();
        NgoDetail detail = NgoDetail.builder()
                .id(id)
                .ngo(ngo)
                .responsibleName("Resp")
                .responsibleEmail("old@mail.com")
                .responsiblePhone("(11) 90000-1111")
                .address("Rua Antiga, 10")
                .build();

        given(repository.existsById(id)).willReturn(true);
        given(repository.save(detail)).willReturn(detail);

        NgoDetail updated = service.update(id, detail);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getResponsibleEmail()).isEqualTo("old@mail.com");
        then(repository).should().existsById(id);
        then(repository).should().save(detail);
    }

    @org.junit.jupiter.api.Test
    void update_shouldThrow_whenNgoDetailDoesNotExist() {
        UUID id = UUID.randomUUID();
        NgoDetail detail = NgoDetail.builder()
                .id(id)
                .responsibleName("Resp")
                .responsibleEmail("resp@mail.com")
                .responsiblePhone("(11) 90000-2222")
                .address("Rua X, 5")
                .build();

        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.update(id, detail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ativo não encontrado.");

        then(repository).should().existsById(id);
    }

    @Test
    void findById_shouldReturnNgoDetail_whenExists() {
        UUID id = UUID.randomUUID();
        Ngo ngo = Ngo.builder().id(UUID.randomUUID()).build();
        NgoDetail detail = NgoDetail.builder()
                .id(id)
                .ngo(ngo)
                .address("Rua Busca, 7")
                .responsibleEmail("find@mail.com")
                .responsibleName("Busca")
                .responsiblePhone("(11) 91111-1111")
                .build();

        given(repository.findById(id)).willReturn(Optional.of(detail));

        NgoDetail found = service.findById(id);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getNgo().getId()).isEqualTo(ngo.getId());
        then(repository).should().findById(id);
    }

    @Test
    void findById_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ativo não encontrado.");

        then(repository).should().findById(id);
    }

    @Test
    void existsById_shouldReturnTrue_whenExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(true);

        boolean exists = service.existsById(id);

        assertThat(exists).isTrue();
        then(repository).should().existsById(id);
    }

    @Test
    void existsById_shouldReturnFalse_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(false);

        boolean exists = service.existsById(id);

        assertThat(exists).isFalse();
        then(repository).should().existsById(id);
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(true);

        service.deleteById(id);

        then(repository).should().existsById(id);
        then(repository).should().deleteById(id);
    }

    @Test
    void deleteById_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.deleteById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ativo não encontrado.");

        then(repository).should().existsById(id);
    }
}
