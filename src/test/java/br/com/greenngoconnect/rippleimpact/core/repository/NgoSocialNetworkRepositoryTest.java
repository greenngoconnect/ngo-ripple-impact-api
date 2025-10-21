package br.com.greenngoconnect.rippleimpact.core.repository;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoSocialNetwork;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class NgoSocialNetworkRepositoryTest {

    @Autowired
    private NgoSocialNetworkRepository repository;

    @Autowired
    private NgoRepository ngoRepository;

    @Autowired
    private NgoCategoryRepository categoryRepository;

    @Test
    void save_shouldPersistSocialLink_forNgo() {
        var cat = categoryRepository.save(NgoCategory.builder()
                .name("Educação")
                .build());

        var ngo = ngoRepository.save(Ngo.builder()
                .ngoCategory(cat)
                .name("ONG Educa")
                .fantasyName("Educa+")
                .description("Desc")
                .companyId("98.765.432/0001-00")
                .email("contato@educa.org")
                .phone("(34) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build());

        var social = repository.save(NgoSocialNetwork.builder()
                .ngo(ngo)
                .instagram("@ong.educa")
                .linkedin("/in/ongeduca")
                .twitterX("@ong_educa")
                .website("https://ongeduca.org")
                .build());

        assertThat(social.getId()).isNotNull();
        assertThat(social.getNgo().getId()).isEqualTo(ngo.getId());
    }
}
