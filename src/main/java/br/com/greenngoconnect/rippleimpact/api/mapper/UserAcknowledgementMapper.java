package br.com.greenngoconnect.rippleimpact.api.mapper;

import br.com.greenngoconnect.rippleimpact.api.dto.request.UserAcknowledgementRequest;
import br.com.greenngoconnect.rippleimpact.api.dto.response.UserAcknowledgementResponse;
import br.com.greenngoconnect.rippleimpact.core.domain.useracknowledgement.UserAcknowledgement;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAcknowledgementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "acknowledgedAt", target = "acknowledgedAt")
    UserAcknowledgement from(UserAcknowledgementRequest userAcknowledgement);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user.id")
    UserAcknowledgementResponse to(UserAcknowledgement userAcknowledgement);

    List<UserAcknowledgementResponse> map(List<UserAcknowledgement> userAcknowledgements);
}
