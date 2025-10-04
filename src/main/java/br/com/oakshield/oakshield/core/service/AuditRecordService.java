package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.auditrecord.AuditRecord;

import java.util.List;
import java.util.UUID;

public interface AuditRecordService {
    AuditRecord create(AuditRecord record);

    List<AuditRecord> findAll();

    AuditRecord update(UUID id, AuditRecord asset);

    AuditRecord findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
