package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.core.domain.risk.Risk;
import br.com.oakshield.oakshield.core.domain.risk.RiskLevel;
import br.com.oakshield.oakshield.core.service.RiskService;
import br.com.oakshield.oakshield.exception.ResourceFoundException;
import br.com.oakshield.oakshield.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RiskServiceImpl implements RiskService {

    private final RiskRepository repository;

    public RiskServiceImpl(RiskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Risk create(Risk risk) {
        if (risk.getAsset().getId() == null) {
            throw new ResourceNotFoundException("Asset ID cannot be null.");
        }

        if (risk.getDescription() == null || risk.getDescription().isBlank()) {
            throw new ResourceNotFoundException("Risk description cannot be null or blank.");
        }

        validCreateIfNotExists(risk.getDescription());

        int calculatedRisk = calculateRisk(risk);
        risk.setRiskScore(calculatedRisk);
        return repository.save(risk);
    }

    @Override
    public List<Risk> findAll() {
        return repository.findAll();
    }

    @Override
    public Risk update(UUID id, Risk risk) {
        boolean riskExists = repository.existsById(id);
        if (!riskExists) {
            throw new IllegalArgumentException("Risk not found.");
        }

        Optional<Risk> riskFound = repository.findById(id);
        if (riskFound.isEmpty()) {
            throw new ResourceNotFoundException("Risk not found.");
        }
        Risk riskUpdate = riskFound.get();
        riskUpdate.update(id, risk);
        int calculatedRisk = calculateRisk(riskUpdate);
        risk.setRiskScore(calculatedRisk);
        return repository.save(riskUpdate);
    }

    @Override
    public Risk findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Risk not found."));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Risk not found.");
        }
        repository.deleteById(id);
    }

    @Override
    public List<Risk> findByAssetId(UUID categoryId) {
        return repository.findByAssetId(categoryId);
    }

    @Override
    public int countRisksByAsset() {
        return repository.countRisksByAsset();
    }

    private int calculateRisk(Risk risk) {
        RiskLevel probability = risk.getProbability();
        int riskProbability = probability.getValue();
        int riskImpact = risk.getImpact().getValue();
        return riskImpact * riskProbability;
    }

    private void validCreateIfNotExists(String description) {
        repository.findByDescription(description)
                .ifPresent(assetType -> {
                    throw new ResourceFoundException("Tipo de ativo já existe com o nome: " + description);
                });
    }
}
