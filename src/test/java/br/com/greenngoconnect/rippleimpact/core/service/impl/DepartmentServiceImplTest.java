package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.department.Department;
import br.com.greenngoconnect.rippleimpact.core.repository.DepartmentRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentServiceImpl service;

    @Test
    void create_shouldPersistDepartment_whenValid() {
        Department department = Department.builder().name("Financeiro").build();
        given(repository.save(department)).willReturn(department);

        Department created = service.create(department);

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Financeiro");
        then(repository).should().save(department);
    }

    @Test
    void create_shouldThrow_whenNameIsNull() {
        Department department = Department.builder().name(null).build();

        assertThatThrownBy(() -> service.create(department))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não pode ser nulo ou vazio");
    }

    @Test
    void create_shouldThrow_whenDepartmentAlreadyExists() {
        String name = "Financeiro";
        Department department = Department.builder().name(name).build();
        given(repository.findByName(name)).willReturn(Optional.of(department));

        assertThatThrownBy(() -> service.create(department))
                .isInstanceOf(ResourceFoundException.class)
                .hasMessageContaining("já existe");
    }

    @Test
    void update_shouldUpdateDepartment_whenExists() {
        UUID id = UUID.randomUUID();
        Department department = Department.builder().id(id).name("Financeiro").build();
        given(repository.existsById(id)).willReturn(true);
        given(repository.save(department)).willReturn(department);

        Department updated = service.update(id, department);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getName()).isEqualTo("Financeiro");
        then(repository).should().existsById(id);
        then(repository).should().save(department);
    }

    @Test
    void update_shouldThrow_whenDepartmentDoesNotExist() {
        UUID id = UUID.randomUUID();
        Department department = Department.builder().id(id).name("Financeiro").build();
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.update(id, department))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");
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
    }

    @Test
    void findById_shouldReturnDepartment_whenExists() {
        UUID id = UUID.randomUUID();
        Department department = Department.builder().id(id).name("Financeiro").build();
        given(repository.findById(id)).willReturn(Optional.of(department));

        Department found = service.findById(id);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getName()).isEqualTo("Financeiro");
        then(repository).should().findById(id);
    }

    @Test
    void findById_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");
    }

    @Test
    void findAll_shouldReturnAllDepartments_whenDataExists() {
        given(repository.findAll()).willReturn(List.of(
                Department.builder().id(UUID.randomUUID()).name("Financeiro").build(),
                Department.builder().id(UUID.randomUUID()).name("RH").build()
        ));

        List<Department> departments = service.findAll();

        assertThat(departments).hasSize(2);
        assertThat(departments).extracting(Department::getName).containsExactlyInAnyOrder("Financeiro", "RH");
        then(repository).should().findAll();
    }
}
