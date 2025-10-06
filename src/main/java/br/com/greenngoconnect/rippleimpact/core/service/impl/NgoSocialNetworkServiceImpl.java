package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoSocialNetwork;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoSocialNetworkRepository;
import br.com.greenngoconnect.rippleimpact.core.service.NgoSocialNetworkService;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NgoSocialNetworkServiceImpl implements NgoSocialNetworkService {

    private final NgoSocialNetworkRepository repository;

    public NgoSocialNetworkServiceImpl(NgoSocialNetworkRepository repository) {
        this.repository = repository;
    }

    @Override
    public NgoSocialNetwork create(NgoSocialNetwork ngoSocialNetwork) {
        return repository.save(ngoSocialNetwork);
    }

    @Override
    public List<NgoSocialNetwork> findAll() {
        return repository.findAll();
    }

    @Override
    public NgoSocialNetwork update(UUID id, NgoSocialNetwork ngoSocialNetwork) {
        boolean ngoSocialNetworkExists = repository.existsById(id);
        if (!ngoSocialNetworkExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        ngoSocialNetwork.setId(id);
        return repository.save(ngoSocialNetwork);
    }

    @Override
    public NgoSocialNetwork findById(UUID id) {
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
}
