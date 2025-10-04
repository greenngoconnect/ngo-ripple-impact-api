package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.core.domain.audit.AuditAdvice;
import br.com.oakshield.oakshield.core.domain.audit.AuditFinding;

import java.util.List;

public interface AiAdvisorOpenAI {
    AuditAdvice analyze(AuditFinding auditFinding, List<String> verdicts);
}
