package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.mitretechnique.MitreTechnique;

import java.util.List;
import java.util.UUID;

public interface MitreTechniqueService {
    MitreTechnique create(MitreTechnique mitreTechnique);

    List<MitreTechnique> findAll();

    MitreTechnique update(UUID id, MitreTechnique mitreTechnique);

    MitreTechnique findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
