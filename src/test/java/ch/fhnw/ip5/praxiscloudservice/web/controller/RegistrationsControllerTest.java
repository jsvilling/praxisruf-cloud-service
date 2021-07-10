package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
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
    private ConfigurationService configurationService;

    @InjectMocks
    private RegistrationsController registrationsController;

    @Test
    void getAllTokens() {
        // When
        registrationsController.getAllKnownTokens();

        // Then
        verify(configurationService, times(1)).getAllKnownTokens();
    }

    @Test
    void findRelevantTokens() {
        // Given
        final PraxisNotification notification = createNotification();

        // When
        registrationsController.findRelevantTokens(notification);

        // Then
        verify(configurationService, times(1)).findAllRelevantTokens(eq(notification));
    }

    @Test
    void register() {
        // When
        registrationsController.register(CLIENT_ID, TOKEN);

        // Then
        verify(configurationService, times(1)).register(CLIENT_ID, TOKEN);
    }

    @Test
    void unregister() {
        // When
        registrationsController.unregister(CLIENT_ID);

        // Then
        verify(configurationService, times(1)).unregister(CLIENT_ID);
    }
}
