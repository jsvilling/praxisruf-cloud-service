package praxiscloudservice.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.api.rulesengine.RulesEngine;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.domain.ClientConfiguration;
import ch.fhnw.ip6.praxisruf.configuration.domain.Registration;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientConfigurationRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.persistence.RegistrationRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.DefaultRegistrationService;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static praxiscloudservice.util.DefaultTestData.*;

@ExtendWith(MockitoExtension.class)
public class DefaultRegistrationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private ClientConfigurationRepository clientConfigurationRepository;

    @Mock
    private RulesEngine rulesEngine;

    @InjectMocks
    private DefaultRegistrationService registrationService;

    @Nested
    class Register {

        @Test
        void register_valid() {
            // Given
            final UUID id = UUID.randomUUID();
            final String token = "TOKEN";
            when(registrationRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

            // When
            registrationService.register(id, token);

            // Then
            verify(registrationRepository, times(1)).save(any());
        }

        @Test
        void register_ClientId() {
            // Given
            final String token = "TOKEN";

            // When
            // Then
            assertThatThrownBy(() -> registrationService.register(null, token))
                    .isInstanceOf(PraxisIntercomException.class);
        }

        @Test
        void register_InvalidToken() {
            // Given
            final UUID id = UUID.randomUUID();

            // When
            // Then
            assertThatThrownBy(() -> registrationService.register(id, null))
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
            registrationService.unregister(id);

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
            assertThatNoException().isThrownBy(() -> registrationService.unregister(id));
        }

        @Test
        void unregister_OtherError() {
            // Given
            final UUID id = UUID.randomUUID();
            final RuntimeException e = new IllegalStateException();
            doThrow(e).when(registrationRepository).deleteById(any(UUID.class));

            // When
            // Then
            assertThatThrownBy(() -> registrationService.unregister(id)).isEqualTo(e);
        }
    }

    @Nested
    class GetAllKnownTokens {

        @Test
        void getAllKnownTokens() {
            // Given
            final Registration registration = createRegistration();
            final List<Registration> registrations = List.of(registration);
            when(registrationRepository.findAll()).thenReturn(registrations);

            // When
            final Set<String> allKnownTokens = registrationService.getAllKnownTokens();

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
            final SendPraxisNotificationDto notification = createSendNotificationDto();
            final ClientConfiguration clientConfiguration = createClientConfiguration();
            final Client client = clientConfiguration.getClient();
            when(clientRepository.findById(any())).thenReturn(Optional.of(createClient()));
            when(clientConfigurationRepository.findAll()).thenReturn(List.of(clientConfiguration));
            when(rulesEngine.isAnyRelevant(any(), eq(notification))).thenReturn(true);
            when(registrationRepository.findByClientId(client.getId())).thenReturn(Optional.of(registration));

            // When
            final Set<RegistrationDto> allKnownTokens = registrationService.findAllRelevantRegistrations(notification);

            // Then
            assertThat(allKnownTokens).extracting(RegistrationDto::getFcmToken).containsExactly(registration.getFcmToken());
        }

        @Test
        void getAllRelevantTokens_ruleIrrelevant() {
            // Given
            final SendPraxisNotificationDto notification = createSendNotificationDto();
            final ClientConfiguration clientConfiguration = createClientConfiguration();
            when(clientConfigurationRepository.findAll()).thenReturn(List.of(clientConfiguration));
            when(rulesEngine.isAnyRelevant(any(), eq(notification))).thenReturn(false);

            // When
            final Set<RegistrationDto> allKnownTokens = registrationService.findAllRelevantRegistrations(notification);

            // Then
            assertThat(allKnownTokens).isEmpty();
        }

        @Test
        void getAllRelevantTokens_noClientConfigurations() {
            // Given
            final SendPraxisNotificationDto notification = createSendNotificationDto();
            when(clientConfigurationRepository.findAll()).thenReturn(List.of());

            // When
            final Set<RegistrationDto> allKnownTokens = registrationService.findAllRelevantRegistrations(notification);

            // Then
            assertThat(allKnownTokens).isEmpty();
        }


        @Test
        void getAllRelevantTokens_noRegistration() {
            // Given
            final SendPraxisNotificationDto notification = createSendNotificationDto();
            final ClientConfiguration clientConfiguration = createClientConfiguration();
            final Client client = clientConfiguration.getClient();
            when(clientConfigurationRepository.findAll()).thenReturn(List.of(clientConfiguration));
            when(rulesEngine.isAnyRelevant(any(), eq(notification))).thenReturn(true);
            when(registrationRepository.findByClientId(client.getId())).thenReturn(Optional.empty());

            // When
            final Set<RegistrationDto> allKnownTokens = registrationService.findAllRelevantRegistrations(notification);

            // Then
            assertThat(allKnownTokens).isEmpty();
        }

    }
    
}
