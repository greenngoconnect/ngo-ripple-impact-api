package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void findByEmail_shouldReturnUser_whenExists() {
        User saved = repository.save(User.builder()
                .name("Ana Silva")
                .email("ana@exemplo.com")
                .password("{noop}123") // ou hash
                // .role(Role.ONG)      // ajuste se existir
                .build());

        var found = repository.findByEmail("ana@exemplo.com");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    void findByName_shouldReturnUser_whenExists() {
        User saved = repository.save(User.builder()
                .name("Joao")
                .email("joao@exemplo.com")
                .password("{noop}abc")
                .build());

        var found = repository.findByName("Joao");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("joao@exemplo.com");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenNotExists() {
        assertThat(repository.findByEmail("naoexiste@ex.com")).isEmpty();
    }

    @Test
    void save_shouldPersistUser_whenValid() {
        User saved = repository.save(User.builder()
                .name("Mariana")
                .email("mariana@exemplo.com")
                .password("{noop}senha")
                .build());

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("mariana@exemplo.com");
    }

    @Test
    void update_shouldUpdateUser_whenExists() {
        User saved = repository.save(User.builder()
                .name("Ricardo")
                .email("ricardo@exemplo.com")
                .password("{noop}pwd")
                .build());

        saved.setName("Ricardo Silva");
        saved.setEmail("ricardo.silva@exemplo.com");
        repository.save(saved);

        var found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ricardo Silva");
        assertThat(found.get().getEmail()).isEqualTo("ricardo.silva@exemplo.com");
    }

    @Test
    void delete_shouldRemoveUser_whenExists() {
        User saved = repository.save(User.builder()
                .name("Clara")
                .email("clara@exemplo.com")
                .password("{noop}1234")
                .build());

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
        assertThat(repository.findByEmail("clara@exemplo.com")).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllUsers_forGivenDataSet() {
        repository.save(User.builder()
                .name("User A")
                .email("a@exemplo.com")
                .password("{noop}a")
                .build());

        repository.save(User.builder()
                .name("User B")
                .email("b@exemplo.com")
                .password("{noop}b")
                .build());

        var all = repository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(User::getName).containsExactlyInAnyOrder("User A", "User B");
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        User saved = repository.save(User.builder()
                .name("Paulo")
                .email("paulo@exemplo.com")
                .password("{noop}pwd")
                .build());

        var found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("paulo@exemplo.com");
    }

}
