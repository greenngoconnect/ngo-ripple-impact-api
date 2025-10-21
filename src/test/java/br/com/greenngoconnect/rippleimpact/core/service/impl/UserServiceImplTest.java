// java
package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import br.com.greenngoconnect.rippleimpact.core.repository.UserRepository;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void create_shouldSaveUser_whenNameIsValidAndDoesNotExist() {
        User user = User.builder().name("Valid Name").build();
        User saved = User.builder().id(UUID.randomUUID()).name("Valid Name").build();

        given(repository.findByName(user.getName())).willReturn(Optional.empty());
        given(repository.save(user)).willReturn(saved);

        User result = service.create(user);

        assertThat(result).isEqualTo(saved);
        then(repository).should().findByName(user.getName());
        then(repository).should().save(user);
    }

    @Test
    void create_shouldThrow_whenNameIsNull() {
        User user = User.builder().name(null).build();

        assertThatThrownBy(() -> service.create(user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Nome do usuário não pode ser nulo ou vazio.");
    }

    @Test
    void create_shouldThrow_whenNameIsBlank() {
        User user = User.builder().name(" ").build();

        assertThatThrownBy(() -> service.create(user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Nome do usuário não pode ser nulo ou vazio.");
    }

    @Test
    void create_shouldThrow_whenNameAlreadyExists() {
        String name = "Existing Name";
        User user = User.builder().name(name).build();
        User existing = User.builder().id(UUID.randomUUID()).name(name).build();

        given(repository.findByName(name)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.create(user))
                .isInstanceOf(ResourceFoundException.class)
                .hasMessageContaining("Usuário com o nome '" + name + "' já existe.");
        then(repository).should().findByName(name);
    }

    @Test
    void findAll_shouldReturnList_whenRepositoryHasUsers() {
        User u1 = User.builder().id(UUID.randomUUID()).name("A").build();
        User u2 = User.builder().id(UUID.randomUUID()).name("B").build();
        List<User> list = Arrays.asList(u1, u2);

        given(repository.findAll()).willReturn(list);

        List<User> result = service.findAll();

        assertThat(result).isEqualTo(list);
        then(repository).should().findAll();
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).name("U").build();

        given(repository.findById(id)).willReturn(Optional.of(user));

        User result = service.findById(id);

        assertThat(result).isEqualTo(user);
        then(repository).should().findById(id);
    }

    @Test
    void findById_shouldThrow_whenUserDoesNotExist() {
        UUID id = UUID.randomUUID();

        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado.");
        then(repository).should().findById(id);
    }

    @Test
    void update_shouldReturnUpdatedUser_whenUserExists() {
        UUID id = UUID.randomUUID();
        User payload = User.builder().name("Updated Name").build();
        User updated = User.builder().id(id).name("Updated Name").build();

        given(repository.existsById(id)).willReturn(true);
        given(repository.save(payload)).willReturn(updated);

        User result = service.update(id, payload);

        assertThat(result).isEqualTo(updated);
        then(repository).should().existsById(id);
        then(repository).should().save(payload);
    }

    @Test
    void update_shouldReturnNull_whenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        User payload = User.builder().name("Nope").build();

        given(repository.existsById(id)).willReturn(false);

        User result = service.update(id, payload);

        assertThat(result).isNull();
        then(repository).should().existsById(id);
    }

    @Test
    void existsById_shouldReturnRepositoryResult() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(true);

        boolean result = service.existsById(id);

        assertThat(result).isTrue();
        then(repository).should().existsById(id);
    }

    @Test
    void deleteById_shouldDeleteUser_whenUserExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(true);

        service.deleteById(id);

        then(repository).should().existsById(id);
        then(repository).should().deleteById(id);
    }

    @Test
    void deleteById_shouldThrow_whenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.deleteById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado.");
        then(repository).should().existsById(id);
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        String email = "u@mail.com";
        User user = User.builder().id(UUID.randomUUID()).email(email).build();

        given(repository.findByEmail(email)).willReturn(Optional.of(user));

        Optional<User> result = service.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
        then(repository).should().findByEmail(email);
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        String email = "no@mail.com";

        given(repository.findByEmail(email)).willReturn(Optional.empty());

        Optional<User> result = service.findByEmail(email);

        assertThat(result).isEmpty();
        then(repository).should().findByEmail(email);
    }
}
