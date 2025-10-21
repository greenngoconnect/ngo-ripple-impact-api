package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class NgoCategoryRepositoryTest {

    @Autowired
    private NgoCategoryRepository repository;

    @Test
    void findByName_shouldReturnCategory_whenExists() {
        NgoCategory cat = repository.save(NgoCategory.builder()
                .name("Educação Ambiental")
                .build());

        assertThat(cat.getId()).isNotNull();

        var found = repository.findByName("Educação Ambiental");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(cat.getId());
    }

    @Test
    void findByName_shouldReturnEmpty_whenNotExists() {
        var result = repository.findByName("Saúde");
        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldPersistCategory_whenValid() {
        NgoCategory cat = repository.saveAndFlush(NgoCategory.builder()
                .name("Assistência Social")
                .build());

        assertThat(cat.getId()).isNotNull();
        assertThat(cat.getName()).isEqualTo("Assistência Social");
    }

    @Test
    void update_shouldUpdateCategoryName_whenExists() {
        NgoCategory saved = repository.saveAndFlush(NgoCategory.builder()
                .name("Cultura")
                .build());

        saved.setName("Cultura e Artes");
        NgoCategory updated = repository.saveAndFlush(saved);

        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getName()).isEqualTo("Cultura e Artes");

        var found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Cultura e Artes");
    }

    @Test
    void delete_shouldRemoveCategory_whenExists() {
        NgoCategory saved = repository.saveAndFlush(NgoCategory.builder()
                .name("Meio Ambiente")
                .build());

        repository.deleteById(saved.getId());

        var found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllCategories_forGivenDataSet() {
        repository.saveAndFlush(NgoCategory.builder().name("Esporte").build());
        repository.saveAndFlush(NgoCategory.builder().name("Lazer").build());

        var all = repository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(NgoCategory::getName).containsExactlyInAnyOrder("Esporte", "Lazer");
    }
}
