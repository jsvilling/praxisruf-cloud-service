package praxiscloudservice.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.MinimalClientDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.DefaultClientService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static praxiscloudservice.util.DefaultTestData.USER_ID;
import static praxiscloudservice.util.DefaultTestData.createClient;

@ExtendWith(MockitoExtension.class)
public class DefaultClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private DefaultClientService clientService;

    @Nested
    class findAvailableClients {
        @Test
        void findAvailableClients_clientFound() {
            // Given
            final Client client = createClient();
            when(clientRepository.findAllByUserId(USER_ID)).thenReturn(Set.of(client));

            // When
            final Set<MinimalClientDto> availableClients = clientService.findByUserId(USER_ID);

            // Then
            final MinimalClientDto expected = MinimalClientDto.builder()
                    .id(client.getClientId())
                    .name(client.getName())
                    .build();
            assertThat(availableClients).containsExactly(expected);
        }

        @Test
        void findAvailableClients_noClientFound() {
            // Given
            when(clientRepository.findAllByUserId(USER_ID)).thenReturn(Set.of());

            // When
            final Set<MinimalClientDto> availableClients = clientService.findByUserId(USER_ID);

            // Then
            assertThat(availableClients).isEmpty();
        }
    }
}
