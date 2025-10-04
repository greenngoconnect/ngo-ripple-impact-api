package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.contact.Organization;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository repository;

    public OrganizationServiceImpl(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Organization create(Organization organization) {
        if (organization.getFullName() == null || organization.getFullName().isEmpty()) {
            throw new ResourceNotFoundException("Nome do ativo não pode ser nulo ou vazio.");
        }

        validCreateIfNotExists(organization.getFullName());
        return repository.save(organization);
    }

    @Override
    public List<Organization> findAll() {
        return repository.findAll();
    }

    @Override
    public Organization update(UUID id, Organization organization) {
        boolean organizationExists = repository.existsById(id);
        if (!organizationExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        organization.setId(id);
        return repository.save(organization);
    }

    @Override
    public Organization findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ativo não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Ativo não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String fullName) {
        repository.findByFullName(fullName)
                .ifPresent(existingOrganization -> {
                    throw new ResourceFoundException("Ativo com o nome '" + fullName + "' já existe.");
                });
    }
}
