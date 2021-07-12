package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.Client;
import ch.fhnw.ip5.praxiscloudservice.domain.ClientConfiguration;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.Registration;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.NotificationTypeRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.RegistrationRepository;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.DefaultConfigurationService;
import ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static ch.fhnw.ip5.praxiscloudservice.util.DefaultTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultConfigurationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientConfigurationRepository clientConfigurationRepository;

    @Mock
    private NotificationTypeRepository notificationTypeRepository;

    @Mock
    private RulesEngine rulesEngine;

    @InjectMocks
    private DefaultConfigurationService configurationService;

    @Nested
    class Register {

        @Test
        void register_valid() {
            // Given
            final UUID id = UUID.randomUUID();
            final String token = "TOKEN";
            when(registrationRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

            // When
            configurationService.register(id, token);

            // Then
            verify(registrationRepository, times(1)).save(any());
        }

        @Test
        void register_ClientId() {
            // Given
            final String token = "TOKEN";

            // When
            // Then
            assertThatThrownBy(() -> configurationService.register(null, token))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void register_InvalidToken() {
            // Given
            final UUID id = UUID.randomUUID();

            // When
            // Then
            assertThatThrownBy(() -> configurationService.register(id, null))
                    .isInstanceOf(PraxisIntercomException.class);
        }

    }

    @Nested
    class Unregister {

        @Test
        void unregister_Existing() {
            // Given
            final UUID id = UUID.randomUUID();

            // When
            configurationService.unregister(id);

            // Then
            verify(registrationRepository, times(1)).deleteById(id);
        }

        @Test
        void unregister_AlreadyUnregistered() {
            // Given
            final UUID id = UUID.randomUUID();
            doThrow(new IllegalArgumentException()).when(registrationRepository).deleteById(any(UUID.class));

            // When
            // Then
            assertThatNoException().isThrownBy(() -> configurationService.unregister(id));
        }

        @Test
        void unregister_OtherError() {
            // Given
            final UUID id = UUID.randomUUID();
            final RuntimeException e = new IllegalStateException();
            doThrow(e).when(registrationRepository).deleteById(any(UUID.class));

            // When
            // Then
            assertThatThrownBy(() -> configurationService.unregister(id)).isEqualTo(e);
        }
    }

    @Nested
    class GetAllKnownTokens {

        @Test
        void getAllKnownTokens() {
            // Given
            final Registration registration = DefaultTestData.createRegistration();
            final List<Registration> registrations = List.of(registration);
            when(registrationRepository.findAll()).thenReturn(registrations);

            // When
            final Set<String> allKnownTokens = configurationService.getAllKnownTokens();

            // Then
            assertThat(allKnownTokens).containsExactly(registration.getFcmToken());
        }

    }

    @Nested
    class GetAllRelevantTokens {

        @Test
        void getAllRelevantTokens_ruleRelevant() {
            // Given
            final Registration registration = createRegistration();
            final PraxisNotification notification = createNotification();
            final ClientConfiguration clientConfiguration = createClientConfiguration();
            final Client client = clientConfiguration.getClient();
            when(clientConfigurationRepository.findAll()).thenReturn(List.of(clientConfiguration));
            when(rulesEngine.isAnyRelevant(any(), eq(notification))).thenReturn(true);
            when(registrationRepository.findByClientId(client.getClientId())).thenReturn(Optional.of(registration));

            // When
            final Set<String> allKnownTokens = configurationService.findAllRelevantTokens(notification);

            // Then
            assertThat(allKnownTokens).containsExactly(registration.getFcmToken());
        }

        @Test
        void getAllRelevantTokens_ruleIrrelevant() {
            // Given
            final PraxisNotification notification = createNotification();
            final ClientConfiguration clientConfiguration = createClientConfiguration();
            when(clientConfigurationRepository.findAll()).thenReturn(List.of(clientConfiguration));
            when(rulesEngine.isAnyRelevant(any(), eq(notification))).thenReturn(false);

            // When
            final Set<String> allKnownTokens = configurationService.findAllRelevantTokens(notification);

            // Then
            assertThat(allKnownTokens).isEmpty();
        }

        @Test
        void getAllRelevantTokens_noClientConfigurations() {
            // Given
            final PraxisNotification notification = createNotification();
            when(clientConfigurationRepository.findAll()).thenReturn(List.of());

            // When
            final Set<String> allKnownTokens = configurationService.findAllRelevantTokens(notification);

            // Then
            assertThat(allKnownTokens).isEmpty();
        }


        @Test
        void getAllRelevantTokens_noRegistration() {
            // Given
            final PraxisNotification notification = createNotification();
            final ClientConfiguration clientConfiguration = createClientConfiguration();
            final Client client = clientConfiguration.getClient();
            when(clientConfigurationRepository.findAll()).thenReturn(List.of(clientConfiguration));
            when(rulesEngine.isAnyRelevant(any(), eq(notification))).thenReturn(true);
            when(registrationRepository.findByClientId(client.getClientId())).thenReturn(Optional.empty());

            // When
            final Set<String> allKnownTokens = configurationService.findAllRelevantTokens(notification);

            // Then
            assertThat(allKnownTokens).isEmpty();
        }

    }

    @Nested
    class findAvailableClients {
        @Test
        void findAvailableClients_clientFound() {
            // Given
            final Client client = createClient();
            when(clientRepository.findAllByUserId(USER_ID)).thenReturn(Set.of(client));

            // When
            final Set<MinimalClientDto> availableClients = configurationService.findAvailableClients(USER_ID);

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
            final Set<MinimalClientDto> availableClients = configurationService.findAvailableClients(USER_ID);

            // Then
            assertThat(availableClients).isEmpty();
        }
    }

    @Nested
    class createClient {



    }

    @Nested
    class createClientConfiguration {



    }

}
