package ch.fhnw.ip6.praxisruf.configuration.service.mapper;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallGroupDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.CallGroup;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class CallGroupMapper {

    public static CallGroupDto toCalLGroupDto(CallGroup callGroup) {
        Set<UUID> participants = callGroup.getParticipants().stream()
                .map(Client::getClientId)
                .collect(Collectors.toSet());

        return CallGroupDto.builder()
                .id(callGroup.getId())
                .name(callGroup.getName())
                .participants(participants)
                .build();
    }

    public static Set<CallGroupDto> toCalLGroupDtos(Collection<CallGroup> callGroup) {
        return callGroup.stream().map(CallGroupMapper::toCalLGroupDto).collect(Collectors.toSet());
    }

}
