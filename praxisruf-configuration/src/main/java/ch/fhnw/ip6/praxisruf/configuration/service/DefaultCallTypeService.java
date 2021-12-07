package ch.fhnw.ip6.praxisruf.configuration.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallTypeDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.CallTypeService;
import ch.fhnw.ip6.praxisruf.configuration.domain.CallType;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.persistence.CallTypeRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.mapper.CallTypeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class DefaultCallTypeService implements CallTypeService {

    private final CallTypeRepository callTypeRepository;
    private final ClientRepository clientRepository;

    @Override
    public CallTypeDto create(CallTypeDto dto) {
        List<Client> participants = clientRepository.findAllById(dto.getParticipants());

        CallType callType = CallType.builder()
                .displayText(dto.getDisplayText())
                .participants(participants)
                .clientConfigurations(Collections.emptySet())
                .build();
        callType = callTypeRepository.save(callType);

        return CallTypeMapper.toCallTypeDto(callType);
    }

    @Override
    public CallTypeDto findById(UUID id) {
        final CallType callType = findExisting(id);
        return CallTypeMapper.toCallTypeDto(callType);
    }

    @Override
    public Set<CallTypeDto> findAll() {
        final List<CallType> callTypes = callTypeRepository.findAll();
        return CallTypeMapper.toCalLTypeDtos(callTypes);
    }

    @Override
    public CallTypeDto update(CallTypeDto dto) {
        final CallType callType = findExisting(dto.getId());
        final List<Client> participants = clientRepository.findAllById(dto.getParticipants());

        callType.setParticipants(participants);
        callType.setDisplayText(dto.getDisplayText());

        return CallTypeMapper.toCallTypeDto(callType);
    }

    @Override
    public void deleteById(UUID id) {
        try {
            callTypeRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            log.info("CallType with id {} already deleted.", id);
        }
    }

    @Override
    public void deleteAllById(Collection<UUID> ids) {
        ids.forEach(this::deleteById);
    }

    private CallType findExisting(UUID id) {
        return callTypeRepository.findById(id)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.CALL_TYPE_NOT_FOUND));
    }
}
