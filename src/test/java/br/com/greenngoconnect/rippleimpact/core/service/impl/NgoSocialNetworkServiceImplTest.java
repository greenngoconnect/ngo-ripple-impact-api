package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.Ngo;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoSocialNetwork;
import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoStatus;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoSocialNetworkRepository;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class NgoSocialNetworkServiceImplTest {

    @Mock
    private NgoSocialNetworkRepository repository;

    @InjectMocks
    private NgoSocialNetworkServiceImpl service;

    @Test
    void create_shouldPersistNgoSocialNetwork_whenValid() {
        Ngo input = Ngo.builder()
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build();

        NgoSocialNetwork socialNetwork = NgoSocialNetwork.builder()
                .ngo(input)
                .linkedin("https://linkedin.com/ong")
                .twitterX("https://twitter.com/ong")
                .instagram("https://instagram.com/ong")
                .website("https://ong.org")
                .build();
        given(repository.save(socialNetwork)).willReturn(socialNetwork);

        NgoSocialNetwork created = service.create(socialNetwork);

        assertThat(created).isNotNull();
        assertThat(created.getLinkedin()).isEqualTo("https://linkedin.com/ong");
        then(repository).should().save(socialNetwork);
    }

    @Test
    void findAll_shouldReturnAllNgoSocialNetworks_whenDataExists() {
        Ngo input = Ngo.builder()
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build();

        NgoSocialNetwork sn1 = NgoSocialNetwork.builder()
                .id(UUID.randomUUID())
                .ngo(input)
                .linkedin("https://linkedin.com/ong")
                .twitterX("https://twitter.com/ong")
                .instagram("https://instagram.com/ong")
                .website("https://ong.org")
                .build();

        NgoSocialNetwork sn2 = NgoSocialNetwork.builder()
                .id(UUID.randomUUID())
                .ngo(input)
                .linkedin("https://linkedin.com/ong2")
                .twitterX("https://twitter.com/ong2")
                .instagram("https://instagram.com/ong2")
                .website("https://ong2.org")
                .build();
        given(repository.findAll()).willReturn(List.of(sn1, sn2));

        List<NgoSocialNetwork> all = service.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(NgoSocialNetwork::getLinkedin).containsExactlyInAnyOrder("https://linkedin.com/ong", "https://linkedin.com/ong2");
        then(repository).should().findAll();
    }

    @Test
    void update_shouldUpdateNgoSocialNetwork_whenExists() {
        UUID id = UUID.randomUUID();

        Ngo input = Ngo.builder()
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build();

        NgoSocialNetwork socialNetwork = NgoSocialNetwork.builder()
                .id(id)
                .ngo(input)
                .linkedin("https://linkedin.com/ong2")
                .twitterX("https://twitter.com/ong2")
                .instagram("https://instagram.com/ong2")
                .website("https://ong2.org")
                .build();
        given(repository.existsById(id)).willReturn(true);
        given(repository.save(socialNetwork)).willReturn(socialNetwork);

        NgoSocialNetwork updated = service.update(id, socialNetwork);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getLinkedin()).isEqualTo("https://linkedin.com/ong2");
        then(repository).should().existsById(id);
        then(repository).should().save(socialNetwork);
    }

    @Test
    void update_shouldThrow_whenNgoSocialNetworkDoesNotExist() {
        UUID id = UUID.randomUUID();

        Ngo input = Ngo.builder()
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build();

        NgoSocialNetwork socialNetwork = NgoSocialNetwork.builder()
                .id(id)
                .ngo(input)
                .linkedin("https://linkedin.com/ong2")
                .twitterX("https://twitter.com/ong2")
                .instagram("https://instagram.com/ong2")
                .website("https://ong2.org")
                .build();
        given(repository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.update(id, socialNetwork))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ativo não encontrado.");

        then(repository).should().existsById(id);
    }

    @Test
    void findById_shouldReturnNgoSocialNetwork_whenExists() {
        UUID id = UUID.randomUUID();

        Ngo input = Ngo.builder()
                .name("ONG Verde")
                .fantasyName("Verde Viva")
                .description("Desc")
                .companyId("12.345.678/0001-90")
                .email("contato@ong.org")
                .phone("(11) 90000-0000")
                .ngoStatus(NgoStatus.ACTIVE)
                .build();

        NgoSocialNetwork socialNetwork = NgoSocialNetwork.builder()
                .id(id)
                .ngo(input)
                .linkedin("https://linkedin.com/ong2")
                .twitterX("https://twitter.com/ong2")
                .instagram("https://instagram.com/ong2")
                .website("https://ong2.org")
                .build();
        given(repository.findById(id)).willReturn(Optional.of(socialNetwork));

        NgoSocialNetwork found = service.findById(id);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getLinkedin()).isEqualTo("https://linkedin.com/ong2");
        then(repository).should().findById(id);
    }

    @Test
    void findById_shouldThrow_whenNgoSocialNetworkDoesNotExist() {
        UUID id = UUID.randomUUID();
        given(repository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ativo não encontrado.");

        then(repository).should().findById(id);
    }

    @Test
    void existsById_shouldReturnTrue_whenExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(true);

        boolean exists = service.existsById(id);

        assertThat(exists).isTrue();
        then(repository).should().existsById(id);
    }

    @Test
    void existsById_shouldReturnFalse_whenNotExists() {
        UUID id = UUID.randomUUID();
        given(repository.existsById(id)).willReturn(false);

        boolean exists = service.existsById(id);

        assertThat(exists).isFalse();
        then(repository).should().existsById(id);
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
                .hasMessageContaining("Ativo não encontrado.");

        then(repository).should().existsById(id);
    }
}
