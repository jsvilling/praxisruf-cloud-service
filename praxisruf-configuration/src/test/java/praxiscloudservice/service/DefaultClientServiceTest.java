package praxiscloudservice.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.ClientDto;
import ch.fhnw.ip6.praxisruf.commons.dto.configuration.MinimalClientDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.domain.Client;
import ch.fhnw.ip6.praxisruf.configuration.persistence.ClientRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.DefaultClientService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static praxiscloudservice.util.DefaultTestData.*;

@ExtendWith(MockitoExtension.class)
public class DefaultClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private DefaultClientService clientService;

    @Test
    void create() {
        // Given
        final ClientDto input = createClientDto();
        input.setId(null); // No id at creation
        when(clientRepository.saveAndFlush(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // When
        final ClientDto result = clientService.create(input);

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(input);
    }

    @Test
    void update() {
        // Given
        final ClientDto input = createClientDto();
        final Client client = createClient();
        when(clientRepository.findById(input.getId())).thenReturn(Optional.of(client));

        // When
        final ClientDto result = clientService.update(input);

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(input);
    }

    @Nested
    class Read {

        @Test
        void findAll_found() {
            // Given
            final Client client = createClient();
            when(clientRepository.findAll()).thenReturn(List.of(client));

            // When
            final Set<ClientDto> result = clientService.findAll();

            // Then
            final ClientDto expected = createClientDto();
            assertThat(result).hasSize(1);
            assertThat(result.iterator().next()).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void findAll_empty() {
            // Given
            when(clientRepository.findAll()).thenReturn(Collections.emptyList());

            // When
            final Set<ClientDto> result = clientService.findAll();

            // Then
            assertThat(result).isNotNull().isEmpty();
        }

        @Test
        void findById_found() {
            // Given
            final Client client = createClient();
            final UUID id = client.getId();
            when(clientRepository.findById(id)).thenReturn(Optional.of(client));

            // When
            final ClientDto result = clientService.findById(id);

            // Then
            final ClientDto expeceted = createClientDto();
            assertThat(result).usingRecursiveComparison().isEqualTo(expeceted);
        }

        @Test
        void findById_notFound() {
            // Given
            final UUID id = UUID.randomUUID();
            when(clientRepository.findById(id)).thenReturn(Optional.empty());

            // When
            // Then
             assertThatThrownBy(() -> clientService.findById(id))
                    .isInstanceOf(PraxisIntercomException.class)
                    .extracting(e -> (PraxisIntercomException) e)
                    .extracting(PraxisIntercomException::getErrorCode)
                    .isEqualTo(ErrorCode.CLIENT_NOT_FOUND);

        }

        @Test
        void findAvailableClients_clientFound() {
            final Client client = createClient();
            when(clientRepository.findAllByUserId(USER_ID)).thenReturn(Set.of(client));

            // When
            final Set<MinimalClientDto> availableClients = clientService.findByUserId(USER_ID);

            // Then
            final MinimalClientDto expected = MinimalClientDto.builder()
                    .id(client.getId())
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
    
    @Nested
    class Delete {

        @Test
        void deleteOne_found() {
            // Given
            final UUID id = UUID.randomUUID();

            // When
            clientService.deleteById(id);

            // Then
            verify(clientRepository).deleteById(id);
        }

        @Test
        void deleteOne_notFound() {
            // Given
            final UUID id = UUID.randomUUID();
            doThrow(new IllegalArgumentException()).when(clientRepository).deleteById(id);

            // When
            clientService.deleteById(id);

            // Then
            verify(clientRepository).deleteById(id);
        }

        @Test
        void deleteOne_exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final UUID id = UUID.randomUUID();
            doThrow(e).when(clientRepository).deleteById(any());

            // When
            // Then
            assertThatThrownBy(() -> clientService.deleteById(id)).isSameAs(e);
        }

        @Test
        void deleteMany_found() {
            // Given
            final UUID id = UUID.randomUUID();

            // When
            clientService.deleteAllById(List.of(id));

            // Then
            verify(clientRepository).deleteById(id);
        }

        @Test
        void deleteMany_notFound() {
            // Given
            final UUID id = UUID.randomUUID();
            doThrow(new IllegalArgumentException()).when(clientRepository).deleteById(id);

            // When
            clientService.deleteAllById(List.of(id));

            // Then
            verify(clientRepository).deleteById(id);
        }

        @Test
        void deleteMany_exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final UUID id = UUID.randomUUID();
            doThrow(e).when(clientRepository).deleteById(id);
            // When
            // Then
            assertThatThrownBy(() -> clientService.deleteAllById(List.of(id))).isSameAs(e);
        }

    }
        
}
