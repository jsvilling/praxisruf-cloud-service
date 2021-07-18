package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.NotificationTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void findNotificationTypesForClient() {
        // Given
        final UUID clientId = UUID.randomUUID();

        // When
        notificationTypeController.findNotificationTypesForClient(clientId);

        // Then
        verify(notificationTypeService, times(1)).findNotificationTypesForClient(eq(clientId));
    }

}
