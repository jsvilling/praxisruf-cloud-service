package ch.fhnw.ip6.praxisruf.configuration.service.mapper;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallTypeDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.CallType;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CallTypeMapper {

    public static CallTypeDto toCallTypeDto(CallType callType) {
        return CallTypeDto.builder()
                .id(callType.getId())
                .displayText(callType.getDisplayText())
                .participants(callType.getParticipants().stream().map(Client::getId).collect(Collectors.toSet()))
                .build();
    }

    public static Set<CallTypeDto> toCalLTypeDtos(Collection<CallType> callType) {
        return callType.stream().map(CallTypeMapper::toCallTypeDto).collect(Collectors.toSet());
    }

}
