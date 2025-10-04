package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.datasubject.DataSubject;
import br.com.oakshield.oakshield.core.service.DataSubjectService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DataSubjectServiceImpl implements DataSubjectService {

    private final DataSubjectRepository repository;

    public DataSubjectServiceImpl(DataSubjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public DataSubject create(DataSubject dataSubject) {
        if (dataSubject.getName() == null || dataSubject.getName().isEmpty()) {
            throw new ResourceNotFoundException("Tipo de ativo não pode ser nulo.");
        }

        validCreateIfNotExists(dataSubject.getName());
        return repository.save(dataSubject);
    }

    @Override
    public List<DataSubject> findAll() {
        return repository.findAll();
    }

    @Override
    public DataSubject update(UUID id, DataSubject dataSubject) {
        boolean dataSubjectExists = repository.existsById(id);
        if (!dataSubjectExists) {
            throw new ResourceNotFoundException("Titular dos dados não encontrado.");
        }
        dataSubject.setId(id);
        return repository.save(dataSubject);
    }

    @Override
    public DataSubject findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Titular dos dados não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Titular dos dados não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByName(name)
                .ifPresent(existingDataSubject -> {
                    throw new ResourceFoundException("Titular dos dados com o nome '" + name + "' já existe.");
                });
    }
}
