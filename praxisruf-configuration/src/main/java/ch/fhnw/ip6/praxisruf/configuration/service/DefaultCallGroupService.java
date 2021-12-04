package ch.fhnw.ip6.praxisruf.configuration.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallGroupDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.CallGroupService;
import ch.fhnw.ip6.praxisruf.configuration.domain.CallGroup;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.persistence.CallGroupRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.mapper.CallGroupMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode.CALL_GROUP_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class DefaultCallGroupService implements CallGroupService {

    private final CallGroupRepository callGroupRepository;
    private final ClientRepository clientRepository;

    @Override
    public CallGroupDto create(CallGroupDto callGroupDto) {
        List<Client> clients = findExistingClient(callGroupDto.getParticipants());
        CallGroup callGroup = CallGroup.builder()
                .name(callGroupDto.getName())
                .participants(clients)
                .build();
        callGroup = callGroupRepository.save(callGroup);
        return CallGroupMapper.toCalLGroupDto(callGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public CallGroupDto findById(UUID callGroupId) {
        final CallGroup callGroup = findExisting(callGroupId);
        return CallGroupMapper.toCalLGroupDto(callGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<CallGroupDto> findAll() {
        final List<CallGroup> callGroups = callGroupRepository.findAll();
        return CallGroupMapper.toCalLGroupDtos(callGroups);
    }

    @Override
    public CallGroupDto update(CallGroupDto callGroupDto) {
        final List<Client> clients = findExistingClient(callGroupDto.getParticipants());
        final CallGroup callGroup = findExisting(callGroupDto.getId());
        callGroup.setName(callGroupDto.getName());
        callGroup.setParticipants(clients);
        return callGroupDto;
    }

    @Override
    public void deleteById(UUID callGroupId) {
        try {
            callGroupRepository.deleteById(callGroupId);
        } catch (IllegalArgumentException e) {
            log.info("CallGroup with id {} was already deleted.", callGroupId);
        }
    }

    @Override
    public void deleteAllById(Collection<UUID> callGroupIds) {
        callGroupIds.forEach(this::deleteById);
    }

    private CallGroup findExisting(UUID id) {
        return callGroupRepository.findById(id).orElseThrow(() -> new PraxisIntercomException(CALL_GROUP_NOT_FOUND));
    }

    private List<Client> findExistingClient(Iterable<UUID> ids) {
        return clientRepository.findAllById(ids);
    }
}
