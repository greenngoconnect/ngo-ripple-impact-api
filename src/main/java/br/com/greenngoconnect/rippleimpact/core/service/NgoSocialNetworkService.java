package br.com.greenngoconnect.rippleimpact.core.service;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoSocialNetwork;

import java.util.List;
import java.util.UUID;

public interface NgoSocialNetworkService {
    NgoSocialNetwork create(NgoSocialNetwork ngoSocialNetwork);

    List<NgoSocialNetwork> findAll();

    NgoSocialNetwork update(UUID id, NgoSocialNetwork ngoSocialNetwork);

    NgoSocialNetwork findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
