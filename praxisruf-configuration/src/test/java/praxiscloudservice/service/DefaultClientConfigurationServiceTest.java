package praxiscloudservice.service;

import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.domain.ClientConfiguration;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientConfigurationRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.DefaultClientClientConfigurationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import praxiscloudservice.util.DefaultTestData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultClientConfigurationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientConfigurationRepository clientConfigurationRepository;

    @InjectMocks
    private DefaultClientClientConfigurationService clientConfigurationService;


    @Nested
    class DeleteById {

        @Test
        void deleteById_Succes() {
            // Given
            final UUID id = UUID.randomUUID();
            final ClientConfiguration configuration = DefaultTestData.createClientConfiguration();
            final Client client = configuration.getClient();
            when(clientConfigurationRepository.findById(any())).thenReturn(Optional.of(configuration));
            when(clientRepository.findByClientConfiguration_Id(any())).thenReturn(Optional.of(client));

            // When
            clientConfigurationService.deleteById(id);

            // Then
            verify(clientConfigurationRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteById_AlreadyDeleted() {
            // Given
            final UUID id = UUID.randomUUID();
            final ClientConfiguration configuration = DefaultTestData.createClientConfiguration();
            final Client client = configuration.getClient();
            when(clientConfigurationRepository.findById(any())).thenReturn(Optional.of(configuration));
            when(clientRepository.findByClientConfiguration_Id(any())).thenReturn(Optional.of(client));
            doThrow(new IllegalArgumentException()).when(clientConfigurationRepository).deleteById(any());

            // When
            // Then
            assertThatNoException().isThrownBy(() -> clientConfigurationService.deleteById(id));
            verify(clientConfigurationRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteById_Exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final UUID id = UUID.randomUUID();
            final ClientConfiguration configuration = DefaultTestData.createClientConfiguration();
            final Client client = configuration.getClient();
            when(clientConfigurationRepository.findById(any())).thenReturn(Optional.of(configuration));
            when(clientRepository.findByClientConfiguration_Id(any())).thenReturn(Optional.of(client));
            doThrow(e).when(clientConfigurationRepository).deleteById(any());

            // When
            // Then
            assertThatThrownBy(() -> clientConfigurationService.deleteById(id))
                    .isSameAs(e);
            verify(clientConfigurationRepository, times(1)).deleteById(id);
        }
    }

    @Nested
    class DeleteAllById {
        @Test
        void deleteAllById_Succes() {
            // Given
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            final ClientConfiguration configuration = DefaultTestData.createClientConfiguration();
            final Client client = configuration.getClient();
            when(clientConfigurationRepository.findById(any())).thenReturn(Optional.of(configuration));
            when(clientRepository.findByClientConfiguration_Id(any())).thenReturn(Optional.of(client));

            // When
            clientConfigurationService.deleteAllById(ids);

            // Then
            verify(clientConfigurationRepository, times(3)).deleteById(any());
        }

        @Test
        void deleteById_AlreadyDeleted() {
            // Given
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            doThrow(new IllegalArgumentException()).when(clientConfigurationRepository).deleteById(any());
            final ClientConfiguration configuration = DefaultTestData.createClientConfiguration();
            final Client client = configuration.getClient();
            when(clientConfigurationRepository.findById(any())).thenReturn(Optional.of(configuration));
            when(clientRepository.findByClientConfiguration_Id(any())).thenReturn(Optional.of(client));

            // When
            // Then
            assertThatNoException().isThrownBy(() -> clientConfigurationService.deleteAllById(ids));
            verify(clientConfigurationRepository, times(3)).deleteById(any());
        }

        @Test
        void deleteById_Exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            doThrow(e).when(clientConfigurationRepository).deleteById(any());
            final ClientConfiguration configuration = DefaultTestData.createClientConfiguration();
            final Client client = configuration.getClient();
            when(clientConfigurationRepository.findById(any())).thenReturn(Optional.of(configuration));
            when(clientRepository.findByClientConfiguration_Id(any())).thenReturn(Optional.of(client));

            // When
            // Then
            assertThatThrownBy(() -> clientConfigurationService.deleteAllById(ids))
                    .isSameAs(e);
            verify(clientConfigurationRepository, times(1)).deleteById(any());
        }
    }
}
