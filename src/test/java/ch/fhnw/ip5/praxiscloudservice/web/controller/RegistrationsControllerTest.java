package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.RegistrationService;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegistrationsControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationsController registrationsController;

    @Test
    void findRelevantTokens() {
        // Given
        final PraxisNotification notification = createNotification();

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
