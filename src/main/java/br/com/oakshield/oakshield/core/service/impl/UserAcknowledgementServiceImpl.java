package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.policy.Policy;
import br.com.oakshield.oakshield.core.domain.useracknowledgement.UserAcknowledgement;
import br.com.oakshield.oakshield.core.domain.user.User;
import br.com.oakshield.oakshield.core.repository.UserAcknowledgementRepository;
import br.com.oakshield.oakshield.core.service.UserAcknowledgementService;
import br.com.oakshield.oakshield.exception.UserPolicyFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserAcknowledgementServiceImpl implements UserAcknowledgementService {

    private final UserAcknowledgementRepository repository;
    private final UserServiceImpl userServiceImpl;
    private final PolicyServiceImpl policyServiceImpl;

    public UserAcknowledgementServiceImpl(UserAcknowledgementRepository repository, UserServiceImpl userServiceImpl, PolicyServiceImpl policyServiceImpl) {
        this.repository = repository;
        this.userServiceImpl = userServiceImpl;
        this.policyServiceImpl = policyServiceImpl;
    }

    @Override
    public void acknowledgePolicy(UUID userId, UUID policyId) {

        UserAcknowledgement acknowledgement = new UserAcknowledgement();
        User userById = userServiceImpl.findById(userId);
        acknowledgement.setUser(userById);

        Policy policyById = policyServiceImpl.findById(policyId);
        acknowledgement.setPolicy(policyById);

        acknowledgement.setAcknowledgedAt(LocalDateTime.now());

        if (repository.existsByUserIdAndPolicyId(userById.getId(), policyById.getId())) {
            throw new UserPolicyFoundException("Usuário já confirmou essa política.");
        }

        repository.save(acknowledgement);
    }

    @Override
    public List<UserAcknowledgement> getAll() {
        return repository.findAll();
    }
}
