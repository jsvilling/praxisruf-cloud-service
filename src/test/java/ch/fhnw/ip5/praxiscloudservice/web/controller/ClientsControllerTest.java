package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientsControllerTest {

    @Mock
    private ConfigurationService configurationService;

    @InjectMocks
    private ClientsController clientsController;

    @Test
    void getAvailableClients() {
        // Given
        final UUID userId = UUID.randomUUID();

        // When
        clientsController.getAvailableClients(userId);

        // Then
        verify(configurationService, times(1)).findAvailableClients(eq(userId));
    }

    @Test
    void createClient() {
        // Given
        final UUID userId = UUID.randomUUID();
        final String clientName = "name";

        // When
        clientsController.createClient(userId, clientName);

        // Then
        verify(configurationService, times(1)).createClient(eq(userId), eq(clientName));
    }

    @Test
    void findNotificationTypesForClient() {
        // Given
        final UUID clientId = UUID.randomUUID();

        // When
        clientsController.findNotificationTypesForClient(clientId);

        // Then
        verify(configurationService, times(1)).findNotificationTypesForClient(eq(clientId));
    }

    @Test
    void createClientConfiguration() {
        // Given
        final String name = "name";
        final ClientConfigurationDto clientConfigurationDto = ClientConfigurationDto.builder()
                .clientId(DefaultTestData.CLIENT_ID)
                .name(name)
                .notificationTypes(Collections.emptyList())
                .ruleParameters(Collections.emptyList())
                .build();

        // When
        clientsController.createClientConfiguration(clientConfigurationDto);

        // Then
        verify(configurationService, times(1)).createClientConfiguration(eq(clientConfigurationDto));
    }
}
