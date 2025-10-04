package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.auditrecord.AuditRecord;
import br.com.oakshield.oakshield.core.service.AuditRecordService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuditRecordServiceImpl implements AuditRecordService {

    private final AuditRecordRepository repository;

    public AuditRecordServiceImpl(AuditRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuditRecord create(AuditRecord record) {
        if (record.getClause() == null || record.getClause().isEmpty()) {
            throw new ResourceNotFoundException("Cláusula do registro de auditoria não pode ser nula ou vazia.");
        }

        validCreateIfNotExists(record.getClause());
        return repository.save(record);
    }

    @Override
    public List<AuditRecord> findAll() {
        return repository.findAll();
    }

    @Override
    public AuditRecord update(UUID id, AuditRecord asset) {
        if (!repository.existsById(id)) {
            return null; // or throw an exception
        }
        asset.setId(id);
        return repository.save(asset);
    }

    @Override
    public AuditRecord findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de auditoria não encontrado."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Registro de auditoria não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validCreateIfNotExists(String clause) {
        repository.findByClause(clause)
                .ifPresent(assetType -> {
                    throw new ResourceFoundException("Registro de auditoria com a cláusula '" + clause + "' já existe.");
                });
    }
}
