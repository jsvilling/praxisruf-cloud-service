package ch.fhnw.ip6.praxisruf.configuration.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.RegistrationDto;
import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;

import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies interactions with Client entities.
 *
 * @author J. Villing
 */
public interface RegistrationService {

    RegistrationDto findById(UUID id);

    /**
     * Creates or Updates a Registration for the given clientId.
     *
     * If a registration for the given clientId exists already, the fcmToken of that Registration is updated.
     * Otherwise a new Registration with the given clientId and fcmToken is created.
     *  @param clientId
     * @param fcmToken
     * @return
     */
    RegistrationDto register(UUID clientId, String fcmToken);

    /**
     * Finds all fcmTokens that are part of an existing Registration.
     *
     * @return Set<String>
     */
    Set<String> getAllKnownTokens();

    /**
     * Finds all fcmTokens that are relevant for the given PraxisNotification.
     *
     * This can be used to determine, which Clients should receive a PraxisNotification. For details on how it is
     * determined whether a Registration and its fcmToken are relevant for a PraxisNotification see RulesEngine and
     * RuleEvaluator and RulesParameter.
     *
     * @param notification
     * @return RegistrationDto
     */
    Set<RegistrationDto> findAllRelevantRegistrations(SendPraxisNotificationDto notification);

    /**
     * Removes the Registration with the given clientId.
     *
     * If no such registration exists, it is assumed that the Registartion was alread removed. In this case the
     * operation terminates silently.
     *
     * @param clientId
     */
    void unregister(UUID clientId);
}
