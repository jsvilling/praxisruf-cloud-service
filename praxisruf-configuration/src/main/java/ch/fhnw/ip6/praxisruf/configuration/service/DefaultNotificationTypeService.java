package ch.fhnw.ip6.praxisruf.configuration.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.NotificationTypeService;
import ch.fhnw.ip6.praxisruf.configuration.domain.NotificationType;
import ch.fhnw.ip6.praxisruf.configuration.persistence.NotificationTypeRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.mapper.NotificationTypesMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class DefaultNotificationTypeService implements NotificationTypeService {

    private final NotificationTypeRepository notificationTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public NotificationTypeDto findById(UUID notificationTypeId) {
        return NotificationTypesMapper.toNotificationTypeDto(findExisting(notificationTypeId));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<NotificationTypeDto> findAll() {
        return NotificationTypesMapper.toNotificationTypeDtos(notificationTypeRepository.findAll());
    }

    @Override
    public NotificationTypeDto create(NotificationTypeDto notificationTypeDto) {
        return NotificationTypesMapper.toNotificationTypeDto(createOrUpdate(notificationTypeDto));
    }

    @Override
    public NotificationTypeDto update(NotificationTypeDto notificationTypeDto) {
        return NotificationTypesMapper.toNotificationTypeDto(createOrUpdate(notificationTypeDto));
    }

    @Override
    public void deleteById(UUID notificationTypeId) {
        try {
            notificationTypeRepository.findById(notificationTypeId)
                    .ifPresent(this::removeNotificationTypeFromRelatedConfigurations);
            notificationTypeRepository.deleteById(notificationTypeId);
        } catch (IllegalArgumentException e) {
            log.info("NotificationType with id {} was already deleted", notificationTypeId);
        }
    }

    @Override
    public void deleteAllById(Collection<UUID> notificationTypeIds) {
        notificationTypeIds.forEach(this::deleteById);
    }

    private NotificationType findExisting(UUID uuid) {
        return notificationTypeRepository.findById(uuid)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.NOTIFICATION_TYPE_NOT_FOUND));
    }

    private void removeNotificationTypeFromRelatedConfigurations(NotificationType notificationType) {
        notificationType.getClientConfigurations()
                .forEach(c -> c.removeNotificationType(notificationType));
    }

    private NotificationType createOrUpdate(NotificationTypeDto notificationTypeDto) {
        final NotificationType updatedNotificationType = NotificationTypesMapper.toNotificationType(notificationTypeDto);
        return notificationTypeRepository.saveAndFlush(updatedNotificationType);
    }
}
