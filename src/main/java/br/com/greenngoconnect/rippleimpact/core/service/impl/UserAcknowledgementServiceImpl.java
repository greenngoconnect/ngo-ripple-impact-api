package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.user.User;
import br.com.greenngoconnect.rippleimpact.core.domain.useracknowledgement.UserAcknowledgement;
import br.com.greenngoconnect.rippleimpact.core.repository.UserAcknowledgementRepository;
import br.com.greenngoconnect.rippleimpact.core.service.UserAcknowledgementService;
import br.com.greenngoconnect.rippleimpact.exception.UserPolicyFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserAcknowledgementServiceImpl implements UserAcknowledgementService {

    private final UserAcknowledgementRepository repository;
    private final UserServiceImpl userServiceImpl;

    public UserAcknowledgementServiceImpl(UserAcknowledgementRepository repository, UserServiceImpl userServiceImpl) {
        this.repository = repository;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public List<UserAcknowledgement> getAll() {
        return repository.findAll();
    }
}
