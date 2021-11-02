package praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.configuration.api.RegistrationService;
import ch.fhnw.ip5.praxiscloudservice.configuration.web.controller.RegistrationsController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import praxiscloudservice.util.DefaultTestData;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static praxiscloudservice.util.DefaultTestData.CLIENT_ID;
import static praxiscloudservice.util.DefaultTestData.TOKEN;

@ExtendWith(MockitoExtension.class)
public class RegistrationsControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationsController registrationsController;

    @Test
    void findRelevantTokens() {
        // Given
        final SendPraxisNotificationDto notification = DefaultTestData.createSendNotificationDto();

        // When
        registrationsController.findRelevantRegistrations(notification);

        // Then
        verify(registrationService, times(1)).findAllRelevantRegistrations(eq(notification));
    }

    @Test
    void register() {
        // When
        registrationsController.register(CLIENT_ID, TOKEN);

        // Then
        verify(registrationService, times(1)).register(CLIENT_ID, TOKEN);
    }

    @Test
    void unregister() {
        // When
        registrationsController.unregister(CLIENT_ID);

        // Then
        verify(registrationService, times(1)).unregister(CLIENT_ID);
    }
}
