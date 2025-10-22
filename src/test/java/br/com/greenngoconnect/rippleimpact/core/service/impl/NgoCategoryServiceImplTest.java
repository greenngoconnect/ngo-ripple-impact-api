package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoCategoryRepository;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
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
public class NgoCategoryServiceImplTest {

    @Mock
    private NgoCategoryRepository repository;

    @InjectMocks
    private NgoCategoryServiceImpl service;

    @org.junit.jupiter.api.Test
    void create_shouldPersistCategory_whenValid() {
        NgoCategory category = NgoCategory.builder().name("Inclusão Digital").build();
        given(repository.findByName("Inclusão Digital")).willReturn(Optional.empty());
        given(repository.save(category)).willReturn(category);

        NgoCategory created = service.create(category);

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Inclusão Digital");
        then(repository).should().findByName("Inclusão Digital");
        then(repository).should().save(category);
    }

    @org.junit.jupiter.api.Test
    void create_shouldThrow_whenNameIsNull() {
        NgoCategory category = NgoCategory.builder().name(null).build();

        assertThatThrownBy(() -> service.create(category))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não pode ser nulo ou vazio");
    }

    @org.junit.jupiter.api.Test
    void create_shouldThrow_whenCategoryAlreadyExists() {
        String name = "Inclusão Digital";
        NgoCategory existing = NgoCategory.builder().name(name).build();
        NgoCategory category = NgoCategory.builder().name(name).build();
        given(repository.findByName(name)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.create(category))
                .isInstanceOf(ResourceFoundException.class)
                .hasMessageContaining("já existe");

        then(repository).should().findByName(name);
    }

    @org.junit.jupiter.api.Test
    void update_shouldUpdateCategory_whenExists() {
        UUID id = UUID.randomUUID();
        NgoCategory category = NgoCategory.builder().id(id).name("Educação").build();
        given(repository.existsById(id)).willReturn(true);
        given(repository.save(category)).willReturn(category);

        NgoCategory updated = service.update(id, category);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getName()).isEqualTo("Educação");
        then(repository).should().existsById(id);
        then(repository).should().save(category);
    }

    @org.junit.jupiter.api.Test
    void update_shouldThrow_whenCategoryDoesNotExist() {
        UUID id = UUID.randomUUID();
        NgoCategory category = NgoCategory.builder().id(id).name("Educação").build();
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.update(id, category))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");

        then(repository).should().existsById(id);
    }

    @org.junit.jupiter.api.Test
    void deleteById_shouldDelete_whenExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(true);

        service.deleteById(id);

        then(repository).should().existsById(id);
        then(repository).should().deleteById(id);
    }

    @org.junit.jupiter.api.Test
    void deleteById_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.deleteById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não encontrado");

        then(repository).should().existsById(id);
    }

    @org.junit.jupiter.api.Test
    void findById_shouldReturnCategory_whenExists() {
        UUID id = UUID.randomUUID();
        NgoCategory category = NgoCategory.builder().id(id).name("Meio Ambiente").build();
        given(repository.findById(id)).willReturn(Optional.of(category));

        NgoCategory found = service.findById(id);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getName()).isEqualTo("Meio Ambiente");
        then(repository).should().findById(id);
    }

    @org.junit.jupiter.api.Test
    void findById_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");

        then(repository).should().findById(id);
    }

    @org.junit.jupiter.api.Test
    void findAll_shouldReturnAllCategories_whenDataExists() {
        given(repository.findAll()).willReturn(List.of(
                NgoCategory.builder().id(UUID.randomUUID()).name("Esporte").build(),
                NgoCategory.builder().id(UUID.randomUUID()).name("Lazer").build()
        ));

        var categories = service.findAll();

        assertThat(categories).hasSize(2);
        assertThat(categories).extracting(NgoCategory::getName).containsExactlyInAnyOrder("Esporte", "Lazer");
        then(repository).should().findAll();
    }
}
