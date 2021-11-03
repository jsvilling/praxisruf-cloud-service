package praxiscloudservice.web.controller;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.configuration.api.NotificationTypeService;
import ch.fhnw.ip6.praxisruf.configuration.web.controller.NotificationTypeController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import praxiscloudservice.util.DefaultTestData;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationTypeControllerTest {

    @Mock
    private NotificationTypeService notificationTypeService;

    @InjectMocks
    private NotificationTypeController notificationTypeController;

    @Test
    void findById(){
        // Given
        final UUID id = UUID.randomUUID();

        // When
        notificationTypeController.findById(id);

        // Then
        verify(notificationTypeService, times(1)).findById(eq(id));
    }

    @Test
    void getAll(){
        // Given
        // When
        notificationTypeController.findAll();

        // Then
        verify(notificationTypeService, times(1)).findAll();
    }

    @Test
    void create() {
        // Given
        final NotificationTypeDto dto = DefaultTestData.createNotificationTypeDto();

        // When
        notificationTypeController.create(dto);

        // Then
        verify(notificationTypeService, times(1)).create(eq(dto));
    }

    @Test
    void updateClientConfiguration() {
        // Given
        final NotificationTypeDto dto = DefaultTestData.createNotificationTypeDto();

        // When
        notificationTypeController.update(dto);

        // Then
        verify(notificationTypeService, times(1)).update(eq(dto));
    }


    @Test
    void delete(){
        // Given
        final UUID id = UUID.randomUUID();

        // When
        notificationTypeController.delete(id);

        // Then
        verify(notificationTypeService, times(1)).deleteById(eq(id));
    }

    @Test
    void deleteMany() {
        // Given
        final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());

        // When
        notificationTypeController.deleteMany(ids);

        // Then
        verify(notificationTypeService, times(1)).deleteAllById(eq(ids));
    }

    @Test
    void findNotificationTypesForClient() {
        // Given
        final UUID clientId = UUID.randomUUID();

        // When
        notificationTypeController.findNotificationTypesForClient(clientId);

        // Then
        verify(notificationTypeService, times(1)).findByClientId(eq(clientId));
    }

}
