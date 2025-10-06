package br.com.greenngoconnect.rippleimpact.core.service;

import br.com.greenngoconnect.rippleimpact.core.domain.ngo.NgoDetail;

import java.util.List;
import java.util.UUID;

public interface NgoDetailService {
    NgoDetail create(NgoDetail ngoDetail);

    List<NgoDetail> findAll();

    NgoDetail update(UUID id, NgoDetail ngoDetail);

    NgoDetail findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
