package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.contact.Organization;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    Organization create(Organization organization);

    List<Organization> findAll();

    Organization update(UUID id, Organization organization);

    Organization findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
