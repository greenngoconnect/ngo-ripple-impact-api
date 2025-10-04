package br.com.oakshield.oakshield.core.service.impl;

import br.com.oakshield.oakshield.api.dto.response.ReportResponse;
import br.com.oakshield.oakshield.core.domain.audit.AuditAdvice;
import br.com.oakshield.oakshield.core.domain.audit.AuditFinding;
import br.com.oakshield.oakshield.core.domain.audit.BusinessSeverity;
import br.com.oakshield.oakshield.core.service.AssetService;
import br.com.oakshield.oakshield.core.service.ReportService;
import br.com.oakshield.oakshield.core.service.RiskService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final AssetService assetService;
    private final RiskService riskService;
    private final AuditFindingRepository auditFindingRepository;
    private final AuditAdviceRepository auditAdviceRepository;

    public ReportServiceImpl(AssetService assetService, RiskService riskService, AuditFindingRepository auditFindingRepository, AuditAdviceRepository auditAdviceRepository) {
        this.assetService = assetService;
        this.riskService = riskService;
        this.auditFindingRepository = auditFindingRepository;
        this.auditAdviceRepository = auditAdviceRepository;
    }

    @Override
    public ReportResponse generateAssetReport() {
        var assets = assetService.findAll();
        List<List<String>> rows = assets.stream()
                .map(asset -> List.of(asset.getName()))
                .toList();

        return ReportResponse.builder()
                .title("Relatório de Ativos por Grupo")
                .generatedAt(LocalDate.now())
                .headers(List.of("Grupo", "Quantidade"))
                .data(rows)
                .generatedBy("system")
                .build();
    }


    @Override
    public ReportResponse generateRiskByAsset() {
        var risks = riskService.findAll();
        List<List<String>> rows = risks.stream()
                .map(risk -> List.of(risk.getAsset().getName(), risk.getDescription(), risk.getProbability().toString()))
                .toList();

        return ReportResponse.builder()
                .title("Relatório de Riscos por Ativo")
                .generatedAt(LocalDate.now())
                .headers(List.of("Ativo", "Descrição do Risco", "Nível de Risco"))
                .data(rows)
                .generatedBy("system")
                .build();
    }

    @Override
    public ComplianceReportResponse generateCompliance(LocalDate from, LocalDate to, String project, String sprint) {
        // Ajuste conforme seus campos de data (CreatedAt/UpdatedAt) em Finding/Advice
        // Aqui suponho que AuditFinding tem createdAt (Instant) — adapte se necessário.
        List<AuditFinding> findings = auditFindingRepository.findAll(); // substitua por busca por período

        // Para cada finding, pegar o último advice (ou o mais severo)
        List<ComplianceReportResponse.Item> items = new ArrayList<>();
        for (AuditFinding f : findings) {
            List<AuditAdvice> advs = auditAdviceRepository.findByAuditFindingId(f.getId());
            if (advs.isEmpty()) continue;
            AuditAdvice latest = advs.get(advs.size() - 1); // regra simples

            items.add(new ComplianceReportResponse.Item(
                    f.getId() != null ? f.getId().toString() : null,
                    f.getService(),
                    f.getRuleId(),
                    f.getResourceName(),
                    f.getEnvironment(),
                    f.isInvolvesPersonalData(),
                    latest.getBusinessSeverity(),
                    latest.getRemediationSteps(),
                    latest.getExplanationPt()
            ));
        }

        // Totais por severidade
        Map<BusinessSeverity, Long> totals = items.stream()
                .collect(Collectors.groupingBy(ComplianceReportResponse.Item::businessSeverity, Collectors.counting()));

        long total = items.size();
        long okCount = total - totals.getOrDefault(BusinessSeverity.CRITICAL,0L)
                - totals.getOrDefault(BusinessSeverity.HIGH,0L);
        double complianceScore = total == 0 ? 100.0 : (okCount * 100.0 / total);

        return new ComplianceReportResponse(
                Optional.ofNullable(project).orElse("OakShield – SGSI"),
                Optional.ofNullable(sprint).orElse("Sprint 2"),
                Instant.now(),
                from != null ? from.toString() : null,
                to != null ? to.toString() : null,
                totals,
                Math.round(complianceScore * 10.0)/10.0,  // 1 casa decimal
                items,
                "Relatório gerado automaticamente. Use como evidência no TCC."
        );
    }
}
