package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.datasubjectrequisition.DataSubjectRequisition;

import java.util.List;
import java.util.UUID;

public interface DataSubjectRequisitionService {
    DataSubjectRequisition create(DataSubjectRequisition dataSubjectRequisition);

    List<DataSubjectRequisition> findAll();

    DataSubjectRequisition update(UUID id, DataSubjectRequisition dataSubjectRequest);

    DataSubjectRequisition findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
