package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoDetail;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class NgoDetailRepositoryTest {

    @Autowired
    private NgoDetailRepository repository;

    @Autowired
    private NgoCategoryRepository categoryRepository;

    @Autowired
    private NgoRepository ngoRepository;

    @Test
    void saveAndFindById_shouldWork() {
        // Se NgoDetail tem relação com Ngo, crie dependências:
        NgoCategory cat = categoryRepository.save(NgoCategory.builder()
                .name("Reflorestamento")
                .build());

        Ngo ngo = ngoRepository.save(Ngo.builder()
                .ngoCategory(cat)
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build());

        NgoDetail detail = repository.save(NgoDetail.builder()
                .ngo(ngo)                 // ajuste se o campo tiver outro nome
                .address("Av. Central, 1000")
                .mission("Proteger o meio ambiente")
                .vision("Um mundo mais verde")
                .values("Sustentabilidade, Educação")
                .responsibleName("João Silva")
                .responsibleEmail("jaoa@mail.com")
                .values("Sustentabilidade, Educação")
                .responsiblePhone("(11) 91234-5678")
                .build());

        var found = repository.findById(detail.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNgo().getId()).isEqualTo(ngo.getId());
    }

    @Test
    void save_shouldPersistNgoDetail_whenValid() {
        NgoCategory cat = categoryRepository.save(NgoCategory.builder()
                .name("Reflorestamento")
                .build());

        Ngo ngo = ngoRepository.save(Ngo.builder()
                .ngoCategory(cat)
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build());

        NgoDetail detail = repository.saveAndFlush(NgoDetail.builder()
                .ngo(ngo)
                .address("Av. Central, 1000")
                .mission("Proteger o meio ambiente")
                .vision("Um mundo mais verde")
                .values("Sustentabilidade, Educação")
                .responsibleName("João Silva")
                .responsibleEmail("joao@mail.com")
                .responsiblePhone("(11) 91234-5678")
                .build());

        assertThat(detail.getId()).isNotNull();

        var found = repository.findById(detail.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNgo().getId()).isEqualTo(ngo.getId());
        assertThat(found.get().getAddress()).isEqualTo("Av. Central, 1000");
        assertThat(found.get().getResponsibleEmail()).isEqualTo("joao@mail.com");
    }

    @Test
    void update_shouldUpdateResponsibleInfo_whenExists() {
        NgoCategory cat = categoryRepository.save(NgoCategory.builder()
                .name("Educação")
                .build());

        Ngo ngo = ngoRepository.save(Ngo.builder()
                .ngoCategory(cat)
                .name("ONG Ensino")
                .fantasyName("Ensina Mais")
                .description("Desc")
                .companyId("98.765.432/0001-11")
                .email("contato@ensina.org")
                .phone("(21) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build());

        NgoDetail saved = repository.saveAndFlush(NgoDetail.builder()
                .ngo(ngo)
                .address("Rua A, 123")
                .mission("Educar crianças")
                .vision("Aprendizado para todos")
                .values("Educação")
                .responsibleName("Maria")
                .responsibleEmail("maria@old.com")
                .responsiblePhone("(21) 90000-1111")
                .build());

        saved.setResponsibleEmail("maria@novo.com");
        saved.setResponsiblePhone("(21) 98888-7777");
        saved.setAddress("Rua B, 456");

        NgoDetail updated = repository.saveAndFlush(saved);

        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getResponsibleEmail()).isEqualTo("maria@novo.com");
        assertThat(updated.getResponsiblePhone()).isEqualTo("(21) 98888-7777");
        assertThat(updated.getAddress()).isEqualTo("Rua B, 456");

        var found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getResponsibleEmail()).isEqualTo("maria@novo.com");
    }

    @Test
    void delete_shouldRemoveNgoDetail_whenExists() {
        NgoCategory cat = categoryRepository.save(NgoCategory.builder()
                .name("Saúde")
                .build());

        Ngo ngo = ngoRepository.save(Ngo.builder()
                .ngoCategory(cat)
                .name("ONG Saúde")
                .fantasyName("Saúde Viva")
                .description("Desc")
                .companyId("33.333.333/0001-33")
                .email("contato@saude.org")
                .phone("(31) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build());

        NgoDetail detail = repository.saveAndFlush(NgoDetail.builder()
                .ngo(ngo)
                .address("Av. Saúde, 10")
                .mission("Cuidar")
                .vision("Bem estar")
                .values("Saúde")
                .responsibleName("Carlos")
                .responsibleEmail("carlos@mail.com")
                .responsiblePhone("(31) 91234-0000")
                .build());

        repository.deleteById(detail.getId());

        var found = repository.findById(detail.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllNgoDetails_forGivenDataSet() {
        NgoCategory cat = categoryRepository.save(NgoCategory.builder()
                .name("Inclusão")
                .build());

        Ngo ngo = ngoRepository.save(Ngo.builder()
                .ngoCategory(cat)
                .name("ONG Inclusão")
                .fantasyName("Inclui")
                .description("Desc")
                .companyId("44.444.444/0001-44")
                .email("contato@inclui.org")
                .phone("(41) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build());

        repository.saveAndFlush(NgoDetail.builder()
                .ngo(ngo)
                .address("Rua 1, 1")
                .mission("Missão A")
                .vision("Visão A")
                .values("Inclusão")
                .responsibleName("Ana")
                .responsibleEmail("ana@mail.com")
                .responsiblePhone("(41) 91111-0001")
                .build());

        repository.saveAndFlush(NgoDetail.builder()
                .ngo(ngo)
                .address("Rua 2, 2")
                .mission("Missão B")
                .vision("Visão B")
                .values("Inclusão")
                .responsibleName("Bruno")
                .responsibleEmail("bruno@mail.com")
                .responsiblePhone("(41) 92222-0002")
                .build());

        var all = repository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(NgoDetail::getAddress).containsExactlyInAnyOrder("Rua 1, 1", "Rua 2, 2");
    }

}