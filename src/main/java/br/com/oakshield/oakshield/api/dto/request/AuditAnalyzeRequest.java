package br.com.oakshield.oakshield.api.dto.request;

import br.com.oakshield.oakshield.core.domain.audit.AuditFinding;

import java.util.List;

public record AuditAnalyzeRequest(
        AuditFinding finding,
        List<String> verdicts
) {}