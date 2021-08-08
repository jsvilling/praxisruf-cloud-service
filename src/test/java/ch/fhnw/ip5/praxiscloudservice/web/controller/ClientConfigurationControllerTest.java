package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ClientConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientConfigurationDto;
import ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientConfigurationControllerTest {

    @Mock
    private ClientConfigurationService clientConfigurationService;

    @InjectMocks
    private ClientConfigurationController clientConfigurationController;


    @Test
    void createClientConfiguration() {
        // Given
        final String name = "name";
        final ClientConfigurationDto clientConfigurationDto = ClientConfigurationDto.builder()
                                                                                    .clientId(DefaultTestData.CLIENT_ID)
                                                                                    .name(name)
                                                                                    .notificationTypes(Collections.emptySet())
                                                                                    .ruleParameters(Collections.emptyList())
                                                                                    .build();

        // When
        clientConfigurationController.createClientConfiguration(clientConfigurationDto);

        // Then
        verify(clientConfigurationService, times(1)).create(eq(clientConfigurationDto));
    }
}
