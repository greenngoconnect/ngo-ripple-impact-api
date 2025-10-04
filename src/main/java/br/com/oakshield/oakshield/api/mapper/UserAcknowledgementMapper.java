package br.com.oakshield.oakshield.api.mapper;

import br.com.oakshield.oakshield.api.dto.request.UserAcknowledgementRequest;
import br.com.oakshield.oakshield.api.dto.response.UserAcknowledgementResponse;
import br.com.oakshield.oakshield.core.domain.useracknowledgement.UserAcknowledgement;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAcknowledgementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "policyId", target = "policy.id")
    @Mapping(source = "acknowledgedAt", target = "acknowledgedAt")
    UserAcknowledgement from(UserAcknowledgementRequest userAcknowledgement);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "policyId", source = "policy.id")
    UserAcknowledgementResponse to(UserAcknowledgement userAcknowledgement);

    List<UserAcknowledgementResponse> map(List<UserAcknowledgement> userAcknowledgements);
}
