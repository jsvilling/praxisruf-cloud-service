package ch.fhnw.ip5.praxiscloudservice.cloud.web.configuration;

import java.util.Set;

/**
 * This interface specifies contracts retrieving the configuration of client devices.
 *
 * @author J. Villing
 */
public interface ConfigurationService {

    void register(String clientId, String fcmToken);

    Set<String> getAllKnownTokens();

}
