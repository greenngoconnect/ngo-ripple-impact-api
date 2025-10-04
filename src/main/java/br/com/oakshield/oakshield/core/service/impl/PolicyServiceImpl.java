package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.policy.Policy;
import br.com.oakshield.oakshield.core.domain.policy.PolicyStatus;
import br.com.oakshield.oakshield.core.service.PolicyService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository repository;

    public PolicyServiceImpl(PolicyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Policy create(Policy policy) {
        if (policy.getName() == null || policy.getName().isEmpty()) {
            throw new ResourceNotFoundException("Política com este Nome já existe.");
        }
        validCreateIfNotExists(policy.getName());
        policy.setPolicyStatus(PolicyStatus.DRAFT);
        return repository.save(policy);
    }

    @Override
    public List<Policy> findAll() {
        return repository.findAll();
    }

    @Override
    public Policy findById(UUID policyId) {
        return repository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Política não encontrada."));
    }

    @Override
    public Policy update(UUID id, Policy policy) {
        if (!repository.existsById(id)) {
            return null; // or throw an exception
        }
        policy.setId(id);
        return repository.save(policy);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Política não encontrada.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByName(name)
                .ifPresent(assetType -> {
                    throw new ResourceFoundException(
                            "Política com o nome '" + name + "' já existe.");
                });
    }
}
