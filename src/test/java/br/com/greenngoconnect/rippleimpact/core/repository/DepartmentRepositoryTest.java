package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.department.Department;
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
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private NgoRepository ngoRepository;

    @Autowired
    private NgoCategoryRepository ngoCategoryRepository;

    @Test
    void findByName_shouldReturnDepartment_whenExists() {

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

        Ngo savadedNgo = ngoRepository.saveAndFlush(ngo);

        Department saved = repository.save(Department.builder()
                .id(null) // JPA gera
                .name("Tecnologia")
                .ngo(savadedNgo)
                .build());

        assertThat(saved.getId()).isNotNull();

        var found = repository.findByName("Tecnologia");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    void findByName_shouldReturnEmpty_whenNotExists() {
        var result = repository.findByName("Inexistente");
        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldPersistDepartment_whenValid() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Saúde").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG Y")
                .email("contato@y.org")
                .phone("(11) 9....")
                .companyId("98.765.432/0001-00")
                .description("...")
                .fantasyName("ONG Y")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build();

        Ngo savedNgo = ngoRepository.saveAndFlush(ngo);

        Department saved = repository.save(Department.builder()
                .id(null)
                .name("Financeiro")
                .ngo(savedNgo)
                .build());

        assertThat(saved.getId()).isNotNull();

        var found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Financeiro");
    }

    @Test
    void update_shouldUpdateDepartmentName_whenExists() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Cidadania").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG Z")
                .email("contato@z.org")
                .phone("(21) 9....")
                .companyId("11.111.111/0001-11")
                .description("...")
                .fantasyName("ONG Z")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build();

        Ngo savedNgo = ngoRepository.saveAndFlush(ngo);

        Department saved = repository.save(Department.builder()
                .id(null)
                .name("RH")
                .ngo(savedNgo)
                .build());

        saved.setName("Recursos Humanos");
        Department updated = repository.saveAndFlush(saved);

        assertThat(updated.getName()).isEqualTo("Recursos Humanos");

        var found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Recursos Humanos");
    }

    @Test
    void delete_shouldRemoveDepartment_whenExists() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Meio Ambiente").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG W")
                .email("contato@w.org")
                .phone("(31) 9....")
                .companyId("22.222.222/0001-22")
                .description("...")
                .fantasyName("ONG W")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build();

        Ngo savedNgo = ngoRepository.saveAndFlush(ngo);

        Department saved = repository.save(Department.builder()
                .id(null)
                .name("Operações")
                .ngo(savedNgo)
                .build());

        repository.deleteById(saved.getId());

        var found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllDepartments_forGivenDataSet() {
        NgoCategory cat = ngoCategoryRepository.save(
                NgoCategory.builder().name("Inclusão").build()
        );

        Ngo ngo = Ngo.builder()
                .name("ONG V")
                .email("contato@v.org")
                .phone("(41) 9....")
                .companyId("33.333.333/0001-33")
                .description("...")
                .fantasyName("ONG V")
                .ngoStatus(NgoStatus.APPROVED)
                .ngoCategory(cat)
                .build();

        Ngo savedNgo = ngoRepository.saveAndFlush(ngo);

        Department d1 = repository.save(Department.builder()
                .id(null)
                .name("Departamento A")
                .ngo(savedNgo)
                .build());

        Department d2 = repository.save(Department.builder()
                .id(null)
                .name("Departamento B")
                .ngo(savedNgo)
                .build());

        var all = repository.findAll();
        assertThat(all).isNotNull();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
        assertThat(all).extracting(Department::getName).containsExactlyInAnyOrder("Departamento A", "Departamento B");
    }

}
