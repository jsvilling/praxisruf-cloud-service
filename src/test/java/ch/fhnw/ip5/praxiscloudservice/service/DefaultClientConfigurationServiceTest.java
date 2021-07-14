package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.RuleParametersRepository;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.DefaultClientClientConfigurationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    @Mock
    private NotificationTypeRepository notificationTypeRepository;

    @Mock
    private RuleParametersRepository ruleParametersRepository;

    @InjectMocks
    private DefaultClientClientConfigurationService clientConfigurationService;

    @Nested
    class DeleteById {

        @Test
        void deleteById_Succes() {
            // Given
            final UUID id = UUID.randomUUID();

            // When
            clientConfigurationService.deleteClientConfigurationById(id);

            // Then
            verify(clientConfigurationRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteById_AlreadyDeleted() {
            // Given
            final UUID id = UUID.randomUUID();
            doThrow(new IllegalArgumentException()).when(clientConfigurationRepository).deleteById(any());

            // When
            // Then
            assertThatNoException().isThrownBy(() -> clientConfigurationService.deleteClientConfigurationById(id));
            verify(clientConfigurationRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteById_Exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final UUID id = UUID.randomUUID();
            doThrow(e).when(clientConfigurationRepository).deleteById(any());

            // When
            // Then
            assertThatThrownBy(() -> clientConfigurationService.deleteClientConfigurationById(id))
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
            // When
            clientConfigurationService.deleteAllClientConfigurationsById(ids);

            // Then
            verify(clientConfigurationRepository, times(3)).deleteById(any());
        }

        @Test
        void deleteById_AlreadyDeleted() {
            // Given
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            doThrow(new IllegalArgumentException()).when(clientConfigurationRepository).deleteById(any());

            // When
            // Then
            assertThatNoException().isThrownBy(() -> clientConfigurationService.deleteAllClientConfigurationsById(ids));
            verify(clientConfigurationRepository, times(3)).deleteById(any());
        }

        @Test
        void deleteById_Exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            doThrow(e).when(clientConfigurationRepository).deleteById(any());

            // When
            // Then
            assertThatThrownBy(() -> clientConfigurationService.deleteAllClientConfigurationsById(ids))
                    .isSameAs(e);
            verify(clientConfigurationRepository, times(1)).deleteById(any());
        }
    }

}
