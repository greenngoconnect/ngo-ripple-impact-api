package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.asset.Asset;
import br.com.oakshield.oakshield.core.domain.datasubject.DataSubject;

import java.util.List;
import java.util.UUID;

public interface DataSubjectService {
    DataSubject create(DataSubject dataSubject);

    List<DataSubject> findAll();

    DataSubject update(UUID id, DataSubject dataSubject);

    DataSubject findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
