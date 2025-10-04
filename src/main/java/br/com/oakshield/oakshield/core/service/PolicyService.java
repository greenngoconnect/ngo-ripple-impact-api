package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.policy.Policy;

import java.util.List;
import java.util.UUID;

public interface PolicyService {
    Policy create(Policy policy);

    List<Policy> findAll();

    Policy findById(UUID policyId);

    Policy update(UUID id, Policy policy);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
