package ch.fhnw.ip5.praxiscloudservice.cloud.web.configuration;

import java.util.Set;

public interface ConfigurationService {

    void register(String clientId, String fcmToken);

    Set<String> getAllKnownTokens();

}
