package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.ClientService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientsControllerTest {

    @Mock
    private ClientService configurationService;

    @InjectMocks
    private ClientsController clientsController;

    @Test
    void getAvailableClients() {
        // Given
        final UUID userId = UUID.randomUUID();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("test",null,null);
        token.setDetails(userId);
        SecurityContextHolder.getContext().setAuthentication(token);
        // When
        clientsController.getAvailableClients();

        // Then
        verify(configurationService, times(1)).findAvailableClients(eq(userId));
    }

    @Test
    void createClient() {
        // Given
        ClientDto dto = ClientDto.builder().id(UUID.randomUUID()).name("name").build();

        // When
        clientsController.createClient(dto);

        // Then
        verify(configurationService, times(1)).createClient(eq(dto));
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

}
