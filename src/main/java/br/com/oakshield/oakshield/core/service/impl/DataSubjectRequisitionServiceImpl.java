package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.datasubjectrequisition.DataSubjectRequisition;
import br.com.oakshield.oakshield.core.domain.datasubjectrequisition.RequestStatus;
import br.com.oakshield.oakshield.core.service.DataSubjectRequisitionService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DataSubjectRequisitionServiceImpl implements DataSubjectRequisitionService {

    private final DataSubjectRequisitionRepository repository;

    public DataSubjectRequisitionServiceImpl(DataSubjectRequisitionRepository repository) {
        this.repository = repository;
    }

    @Override
    public DataSubjectRequisition create(DataSubjectRequisition dataSubjectRequisition) {
        if (dataSubjectRequisition.getDataSubject().getId() == null) {
            throw new ResourceNotFoundException("Tipo de Titulo dos dados não pode ser nulo.");
        }

        validCreateIfNotExists(dataSubjectRequisition.getDataSubject().getName());
        dataSubjectRequisition.setRequestStatus(RequestStatus.OPEN);
        dataSubjectRequisition.setClosedAt(OffsetDateTime.now());
        dataSubjectRequisition.setDueAt(OffsetDateTime.now().plusDays(15));
        return repository.save(dataSubjectRequisition);
    }

    @Override
    public List<DataSubjectRequisition> findAll() {
        return repository.findAll();
    }

    @Override
    public DataSubjectRequisition update(UUID id, DataSubjectRequisition dataSubjectRequest) {
        boolean dataSubjectRequestExists = repository.existsById(id);
        if (!dataSubjectRequestExists) {
            throw new ResourceNotFoundException("Titulo dos dados não encontrado.");
        }
        dataSubjectRequest.setId(id);
        return repository.save(dataSubjectRequest);
    }

    @Override
    public DataSubjectRequisition findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Titulo dos dados não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Titulo dos dados não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String name) {
        repository.findByDataSubjectName(name)
                .ifPresent(existingDataSubjectRequest -> {
                    throw new ResourceFoundException("Titulo dos dados com o nome '" + name + "' já existe.");
                });
    }
}
