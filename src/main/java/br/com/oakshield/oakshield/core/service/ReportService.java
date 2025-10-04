package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.api.dto.response.ReportResponse;

import java.time.LocalDate;

public interface ReportService {

    ReportResponse generateAssetReport();
    ReportResponse generateRiskByAsset();
    ComplianceReportResponse generateCompliance(LocalDate from, LocalDate to, String project, String sprint);
}
