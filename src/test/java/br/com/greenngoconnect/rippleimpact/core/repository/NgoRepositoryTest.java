package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class NgoRepositoryTest {

    @Autowired
    private NgoRepository ngoRepository;

    @Autowired
    private NgoCategoryRepository ngoCategoryRepository;

    @Test
    void shouldSaveNgoWithCategory() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Educação Ambiental").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG X")
                .email("root@x.com")
                .phone("(11) 9....")
                .companyId("12.345.678/0001-90")
                .description("...")
                .fantasyName("ONG X")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat) // << obrigatório
                .build();

        Ngo saved = ngoRepository.saveAndFlush(ngo);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findByName_shouldReturnNgo_whenExists() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Educação Ambiental").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG Verde")
                .email("root@x.com")
                .phone("(11) 9....")
                .companyId("12.345.678/0001-90")
                .description("...")
                .fantasyName("ONG X")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat) // << obrigatório
                .build();

        Ngo saved = ngoRepository.saveAndFlush(ngo);
        var found = ngoRepository.findByName("ONG Verde");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    void update_shouldUpdateNgoName_whenExists() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Comunidade").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG Antiga")
                .email("old@ong.com")
                .phone("(11) 9....")
                .companyId("22.333.444/0001-55")
                .description("...")
                .fantasyName("Antiga")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build();

        Ngo saved = ngoRepository.saveAndFlush(ngo);

        saved.setName("ONG Nova");
        Ngo updated = ngoRepository.saveAndFlush(saved);

        assertThat(updated.getId()).isEqualTo(saved.getId());
        var foundNew = ngoRepository.findByName("ONG Nova");
        var foundOld = ngoRepository.findByName("ONG Antiga");
        assertThat(foundNew).isPresent();
        assertThat(foundNew.get().getId()).isEqualTo(saved.getId());
        assertThat(foundOld).isEmpty();
    }

    @Test
    void delete_shouldRemoveNgo_whenExists() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Cidadania").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG Para Deletar")
                .email("del@ong.com")
                .phone("(11) 9....")
                .companyId("55.666.777/0001-88")
                .description("...")
                .fantasyName("Deletar")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build();

        Ngo saved = ngoRepository.saveAndFlush(ngo);

        ngoRepository.deleteById(saved.getId());

        var found = ngoRepository.findById(saved.getId());
        var foundByName = ngoRepository.findByName("ONG Para Deletar");
        assertThat(found).isEmpty();
        assertThat(foundByName).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllNgos_forGivenDataSet() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Voluntariado").build()
        );

        ngoRepository.saveAndFlush(Ngo.builder()
                .name("ONG A")
                .email("a@ong.com")
                .phone("(11) 9....")
                .companyId("11.111.111/0001-11")
                .description("...")
                .fantasyName("A")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build());

        ngoRepository.saveAndFlush(Ngo.builder()
                .name("ONG B")
                .email("b@ong.com")
                .phone("(22) 9....")
                .companyId("22.222.222/0001-22")
                .description("...")
                .fantasyName("B")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build());

        var all = ngoRepository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(Ngo::getName).containsExactlyInAnyOrder("ONG A", "ONG B");
    }
}