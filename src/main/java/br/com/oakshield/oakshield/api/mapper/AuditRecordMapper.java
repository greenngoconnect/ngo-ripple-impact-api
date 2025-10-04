package br.com.oakshield.oakshield.api.mapper;

import br.com.oakshield.oakshield.api.dto.request.AuditRecordRequest;
import br.com.oakshield.oakshield.api.dto.response.AuditRecordResponse;
import br.com.oakshield.oakshield.core.domain.auditrecord.AuditRecord;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "clause", target = "clause")
    @Mapping(source = "evidence", target = "evidence")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "auditor", target = "auditor")
    @Mapping(source = "auditStatus", target = "auditStatus")
    AuditRecord from(AuditRecordRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    AuditRecordResponse to(AuditRecord auditRecord);

    List<AuditRecordResponse> map(List<AuditRecord> auditRecords);
}
