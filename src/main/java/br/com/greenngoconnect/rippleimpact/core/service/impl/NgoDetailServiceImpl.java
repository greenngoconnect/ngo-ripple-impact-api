package br.com.greenngoconnect.rippleimpact.core.service.impl;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoDetail;
import br.com.greenngoconnect.rippleimpact.core.repository.NgoDetailRepository;
import br.com.greenngoconnect.rippleimpact.core.service.NgoDetailService;
import br.com.greenngoconnect.rippleimpact.exception.ResourceFoundException;
import br.com.greenngoconnect.rippleimpact.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NgoDetailServiceImpl implements NgoDetailService {

    private final NgoDetailRepository repository;

    public NgoDetailServiceImpl(NgoDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public NgoDetail create(NgoDetail ngoDetail) {
        return repository.save(ngoDetail);
    }

    @Override
    public List<NgoDetail> findAll() {
        return repository.findAll();
    }

    @Override
    public NgoDetail update(UUID id, NgoDetail ngoDetail) {
        boolean ngoDetailExists = repository.existsById(id);
        if (!ngoDetailExists) {
            throw new ResourceNotFoundException("Ativo não encontrado.");
        }
        ngoDetail.setId(id);
        return repository.save(ngoDetail);
    }

    @Override
    public NgoDetail findById(UUID id) {
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
