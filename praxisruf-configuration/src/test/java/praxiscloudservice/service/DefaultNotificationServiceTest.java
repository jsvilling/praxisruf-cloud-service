package praxiscloudservice.service;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.NotificationTypeDto;
import ch.fhnw.ip6.praxisruf.commons.exception.ErrorCode;
import ch.fhnw.ip6.praxisruf.commons.exception.PraxisIntercomException;
import ch.fhnw.ip6.praxisruf.configuration.domain.NotificationType;
import ch.fhnw.ip6.praxisruf.configuration.persistence.NotificationTypeRepository;
import ch.fhnw.ip6.praxisruf.configuration.service.DefaultNotificationTypeService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import praxiscloudservice.util.DefaultTestData;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static praxiscloudservice.util.DefaultTestData.createNotificationType;
import static praxiscloudservice.util.DefaultTestData.createNotificationTypeDto;


@ExtendWith(MockitoExtension.class)
public class DefaultNotificationServiceTest {

    @Mock
    private NotificationTypeRepository notificationTypeRepository;

    @InjectMocks
    private DefaultNotificationTypeService notificationTypeService;

    @Test
    void create() {
        // Given
        final NotificationTypeDto input = createNotificationTypeDto();
        when(notificationTypeRepository.saveAndFlush(any())).thenAnswer(returnsFirstArg());

        // When
        final NotificationTypeDto result = notificationTypeService.create(input);

        // Then
        final NotificationTypeDto expected = createNotificationTypeDto();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        verify(notificationTypeRepository).saveAndFlush(any(NotificationType.class));
    }

    @Nested
    class Update {

        @Test
        void update_notFound() {
            // Given
            NotificationTypeDto input = createNotificationTypeDto();
            final UUID id = input.getId();
            when(notificationTypeRepository.findById(id)).thenReturn(Optional.empty());

            // When
            // Then
            assertThatThrownBy(() -> notificationTypeService.findById(id))
                    .isInstanceOf(PraxisIntercomException.class)
                    .extracting(e -> (PraxisIntercomException) e)
                    .extracting(PraxisIntercomException::getErrorCode)
                    .isEqualTo(ErrorCode.NOTIFICATION_TYPE_NOT_FOUND);
        }

        @Test
        void update_success() {
            // Given
            NotificationTypeDto input = createNotificationTypeDto();
            NotificationType notificationType = createNotificationType();
            notificationType.setBody("Other");
            notificationType.setTitle("Other");
            notificationType.setTextToSpeech(true);
            final UUID id = input.getId();
            when(notificationTypeRepository.saveAndFlush(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

            // When
            NotificationTypeDto result = notificationTypeService.update(input);

            // Then
            assertThat(result).usingRecursiveComparison().isEqualTo(input);
            verify(notificationTypeRepository).saveAndFlush(any(NotificationType.class));
        }

    }

    @Nested
    class Read {
        @Test
        void testFindById_found() {
            // Given
            final NotificationType notificationType = createNotificationType();
            final UUID id = notificationType.getId();
            when(notificationTypeRepository.findById(id)).thenReturn(Optional.of(notificationType));

            // When
            final NotificationTypeDto result = notificationTypeService.findById(id);

            // Then
            final NotificationTypeDto expected = DefaultTestData.createNotificationTypeDto();
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void testFindById_notFound() {
            // Given
            final UUID id = UUID.randomUUID();
            when(notificationTypeRepository.findById(id)).thenReturn(Optional.empty());

            // When
            // Then
            assertThatThrownBy(() -> notificationTypeService.findById(id))
                    .isInstanceOf(PraxisIntercomException.class)
                    .extracting(e -> (PraxisIntercomException) e)
                    .extracting(PraxisIntercomException::getErrorCode)
                    .isEqualTo(ErrorCode.NOTIFICATION_TYPE_NOT_FOUND);
        }

        @Test
        void testFindAll_found() {
            // Given
            final NotificationType notificationType = createNotificationType();
            when(notificationTypeRepository.findAll()).thenReturn(List.of(notificationType));

            // When
            final Set<NotificationTypeDto> result = notificationTypeService.findAll();

            // Then
            final NotificationTypeDto expected = DefaultTestData.createNotificationTypeDto();
            assertThat(result).hasSize(1);
            assertThat(result.iterator().next()).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void testFindAll_empty() {
            // Given
            when(notificationTypeRepository.findAll()).thenReturn(emptyList());

            // When
            final Set<NotificationTypeDto> result = notificationTypeService.findAll();

            // Then
            assertThat(result).isNotNull().isEmpty();
        }
    }

    @Nested
    class Delete {

        @Test
        void deleteOne_found() {
            // Given
            final NotificationType notificationType = createNotificationType();
            final UUID id = notificationType.getId();
            when(notificationTypeRepository.findById(id)).thenReturn(Optional.of(notificationType));

            // When
            notificationTypeService.deleteById(id);

            // Then
            verify(notificationTypeRepository).deleteById(id);
        }

        @Test
        void deleteOne_notFound() {
            // Given
            final UUID id = UUID.randomUUID();
            when(notificationTypeRepository.findById(id)).thenReturn(Optional.empty());
            doThrow(new IllegalArgumentException()).when(notificationTypeRepository).deleteById(id);

            // When
            notificationTypeService.deleteById(id);

            // Then
            verify(notificationTypeRepository).deleteById(id);
        }

        @Test
        void deleteOne_exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final UUID id = UUID.randomUUID();
            when(notificationTypeRepository.findById(id)).thenThrow(e);

            // When
            // Then
            assertThatThrownBy(() -> notificationTypeService.deleteById(id)).isSameAs(e);
        }

        @Test
        void deleteMany_found() {
            // Given
            final NotificationType notificationType = createNotificationType();
            final UUID id = notificationType.getId();
            when(notificationTypeRepository.findById(id)).thenReturn(Optional.of(notificationType));

            // When
            notificationTypeService.deleteAllById(List.of(id));

            // Then
            verify(notificationTypeRepository).deleteById(id);
        }

        @Test
        void deleteMany_notFound() {
            // Given
            final UUID id = UUID.randomUUID();
            when(notificationTypeRepository.findById(id)).thenReturn(Optional.empty());
            doThrow(new IllegalArgumentException()).when(notificationTypeRepository).deleteById(id);

            // When
            notificationTypeService.deleteAllById(List.of(id));

            // Then
            verify(notificationTypeRepository).deleteById(id);
        }

        @Test
        void deleteMany_exception() {
            // Given
            final RuntimeException e = new RuntimeException();
            final UUID id = UUID.randomUUID();
            when(notificationTypeRepository.findById(id)).thenThrow(e);

            // When
            // Then
            assertThatThrownBy(() -> notificationTypeService.deleteAllById(List.of(id))).isSameAs(e);        }

    }

}
