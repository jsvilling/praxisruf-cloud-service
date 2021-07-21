package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.RegistrationDto;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;

import java.util.Set;
import java.util.UUID;

/**
 * This interface specifies interactions with Client entities.
 *
 * @author J. Villing & K. Zellweger
 */
public interface RegistrationService {

    /**
     * Creates or Updates a Registration for the given clientId.
     *
     * If a registration for the given clientId exists already, the fcmToken of that Registration is updated.
     * Otherwise a new Registration with the given clientId and fcmToken is created.
     *
     * @param clientId
     * @param fcmToken
     */
    void register(UUID clientId, String fcmToken);

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
    Set<RegistrationDto> findAllRelevantRegistrations(PraxisNotification notification);

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
