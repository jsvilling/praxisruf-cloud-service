package ch.fhnw.ip5.praxiscloudservice.service.configuration;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationTypeService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.NotificationTypeDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.NotificationType;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static ch.fhnw.ip5.praxiscloudservice.service.configuration.mapper.NotificationTypesMapper.*;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultNotificationTypeService implements NotificationTypeService {

    private final NotificationTypeRepository notificationTypeRepository;

    @Override
    public NotificationTypeDto findById(UUID notificationTypeId) {
        return toNotificationTypeDto(findExisting(notificationTypeId));
    }

    @Override
    public Set<NotificationTypeDto> findAll() {
        return toNotificationTypeDtos(notificationTypeRepository.findAll());
    }

    @Override
    public NotificationTypeDto create(NotificationTypeDto notificationTypeDto) {
        final NotificationType notificationType = toNotificationType(notificationTypeDto);
        final NotificationType persistedNotificationType = notificationTypeRepository.saveAndFlush(notificationType);
        return toNotificationTypeDto(persistedNotificationType);
    }

    @Override
    public NotificationTypeDto update(NotificationTypeDto notificationTypeDto) {
        findExisting(notificationTypeDto.getId());
        final NotificationType updatedNotificationType = toNotificationType(notificationTypeDto);
        final NotificationType persistedNotificationType = notificationTypeRepository.saveAndFlush(updatedNotificationType);
        return toNotificationTypeDto(persistedNotificationType);
    }

    @Override
    public void deleteById(UUID notificationTypeId) {
        try {
            notificationTypeRepository.deleteById(notificationTypeId);
        } catch (IllegalArgumentException e) {
            log.info("NotificationType with id {} was already deleted", notificationTypeId);
        }
    }

    @Override
    public void deleteManyById(Collection<UUID> notificationTypeIds) {
        notificationTypeIds.forEach(this::deleteById);
    }

    private NotificationType findExisting(UUID uuid) {
        return notificationTypeRepository.findById(uuid)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.NOTIFICATION_TYPE_NOT_FOUND));
    }
}
