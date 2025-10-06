package br.com.greenngoconnect.rippleimpact.core.service;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoCategory;

import java.util.List;
import java.util.UUID;

public interface NgoCategoryService {
    NgoCategory create(NgoCategory ngoCategory);

    List<NgoCategory> findAll();

    NgoCategory update(UUID id, NgoCategory ngoCategory);

    NgoCategory findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
