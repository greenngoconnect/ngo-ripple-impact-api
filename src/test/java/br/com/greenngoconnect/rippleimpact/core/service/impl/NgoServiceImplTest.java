package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoStatus;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoRepository;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class NgoServiceImplTest {

    @Mock
    private NgoRepository repository;

    @InjectMocks
    private NgoServiceImpl service;

    @Test
    void create_shouldSave_whenNameIsValidAndUnique() {
        // Arrange
        Ngo input = Ngo.builder()
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build();

        given(repository.findByName("ONG Verde")).willReturn(Optional.empty());
        given(repository.save(any(Ngo.class))).willAnswer(inv -> {
            Ngo n = inv.getArgument(0);
            n.setId(UUID.randomUUID());
            return n;
        });

        // Act
        Ngo created = service.create(input);

        // Assert
        assertThat(created.getId()).isNotNull();
        then(repository).should().findByName("ONG Verde");
        then(repository).should().save(any(Ngo.class));
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void create_shouldThrow_whenNameIsNullOrEmpty() {
        Ngo invalid = Ngo.builder().name("").build();
        assertThatThrownBy(() -> service.create(invalid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não pode ser nulo ou vazio");
        then(repository).shouldHaveNoInteractions();
    }

    @Test
    void create_shouldThrow_whenNameAlreadyExists() {
        Ngo input = Ngo.builder().name("ONG Verde").build();
        given(repository.findByName("ONG Verde"))
                .willReturn(Optional.of(Ngo.builder().name("ONG Verde").build()));

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(ResourceFoundException.class)
                .hasMessageContaining("já existe");

        then(repository).should().findByName("ONG Verde");
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void update_shouldSave_whenExists() {
        UUID id = UUID.randomUUID();
        Ngo toUpdate = Ngo.builder().name("Novo Nome").build();
        given(repository.existsById(id)).willReturn(true);
        given(repository.save(any(Ngo.class))).willAnswer(inv -> inv.getArgument(0));

        Ngo updated = service.update(id, toUpdate);

        assertThat(updated.getId()).isEqualTo(id);
        then(repository).should().existsById(id);
        then(repository).should().save(any(Ngo.class));
    }

    @Test
    void update_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.update(id, Ngo.builder().build()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");

        then(repository).should().existsById(id);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findById_shouldReturnNgo_whenExists() {
        UUID id = UUID.randomUUID();
        Ngo ngo = Ngo.builder().id(id).name("ONG Existente").build();
        given(repository.findById(id)).willReturn(Optional.of(ngo));

        Ngo found = service.findById(id);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getName()).isEqualTo("ONG Existente");
        then(repository).should().findById(id);
    }

    @Test
    void findById_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");

        then(repository).should().findById(id);
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
                .hasMessageContaining("não encontrado");

        then(repository).should().existsById(id);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findAll_shouldReturnAllNgos_whenDataExists() {
        given(repository.findAll()).willReturn(List.of(
                Ngo.builder().id(UUID.randomUUID()).name("ONG A").build(),
                Ngo.builder().id(UUID.randomUUID()).name("ONG B").build()
        ));

        List<Ngo> ngos = service.findAll();

        assertThat(ngos).hasSize(2);
        assertThat(ngos).extracting(Ngo::getName).containsExactlyInAnyOrder("ONG A", "ONG B");
        then(repository).should().findAll();
    }

}
