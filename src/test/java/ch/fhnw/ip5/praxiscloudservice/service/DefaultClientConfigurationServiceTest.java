package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.DefaultClientClientConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        @Disabled
        @Test
        void deleteById_Succes() {
            // Given
            final UUID id = UUID.randomUUID();
            final Client client = DefaultTestData.createClient();
            when(clientRepository.findByClientConfiguration_ClientConfigurationId(any())).thenReturn(Optional.of(client));

            // When
            clientConfigurationService.deleteById(id);

            // Then
            verify(clientConfigurationRepository, times(1)).deleteById(id);
        }

        @Disabled
        @Test
        void deleteById_AlreadyDeleted() {
            // Given
            final UUID id = UUID.randomUUID();
            final Client client = DefaultTestData.createClient();
            when(clientRepository.findByClientConfiguration_ClientConfigurationId(any())).thenReturn(Optional.of(client));
            doThrow(new IllegalArgumentException()).when(clientConfigurationRepository).deleteById(any());

            // When
            // Then
            assertThatNoException().isThrownBy(() -> clientConfigurationService.deleteById(id));
            verify(clientConfigurationRepository, times(1)).deleteById(id);
        }

        @Disabled
        @Test
        void deleteById_Exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final UUID id = UUID.randomUUID();
            final Client client = DefaultTestData.createClient();
            when(clientRepository.findByClientConfiguration_ClientConfigurationId(any())).thenReturn(Optional.of(client));
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
        @Disabled
        @Test
        void deleteAllById_Succes() {
            // Given
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            final Client client = DefaultTestData.createClient();
            when(clientRepository.findByClientConfiguration_ClientConfigurationId(any())).thenReturn(Optional.of(client));

            // When
            clientConfigurationService.deleteAllById(ids);

            // Then
            verify(clientConfigurationRepository, times(3)).deleteById(any());
        }

        @Disabled
        @Test
        void deleteById_AlreadyDeleted() {
            // Given
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            doThrow(new IllegalArgumentException()).when(clientConfigurationRepository).deleteById(any());
            final Client client = DefaultTestData.createClient();
            when(clientRepository.findByClientConfiguration_ClientConfigurationId(any())).thenReturn(Optional.of(client));

            // When
            // Then
            assertThatNoException().isThrownBy(() -> clientConfigurationService.deleteAllById(ids));
            verify(clientConfigurationRepository, times(3)).deleteById(any());
        }

        @Disabled
        @Test
        void deleteById_Exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            doThrow(e).when(clientConfigurationRepository).deleteById(any());
            final Client client = DefaultTestData.createClient();
            when(clientRepository.findByClientConfiguration_ClientConfigurationId(any())).thenReturn(Optional.of(client));

            // When
            // Then
            assertThatThrownBy(() -> clientConfigurationService.deleteAllById(ids))
                    .isSameAs(e);
            verify(clientConfigurationRepository, times(1)).deleteById(any());
        }
    }
}
