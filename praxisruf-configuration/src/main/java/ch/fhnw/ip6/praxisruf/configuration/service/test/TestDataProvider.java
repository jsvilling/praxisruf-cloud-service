package ch.fhnw.ip6.praxisruf.configuration.service.test;

import ch.fhnw.ip6.praxisruf.configuration.domain.*;
import ch.fhnw.ip6.praxisruf.configuration.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Service
@AllArgsConstructor
@Profile("init-test-data")
public class TestDataProvider {

    private static final UUID USER_ID = UUID.fromString("3b5587f3-9dbb-4a9c-b3a3-375607ca13a5");

    private final ClientRepository clientRepository;
    private final ClientConfigurationRepository clientConfigurationRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final RuleParametersRepository ruleParametersRepository;
    private final CallTypeRepository callTypeRepository;

    @PostConstruct
    public void createTestData() {
        reset();

        NotificationType alarm = createNotificationTypes("Alarm", "Schlimme Sache", "Alarm");
        NotificationType nextPatient = createNotificationTypes("Nächster Patient", "Nächster Patient bereit", "Nächster Patient");
        NotificationType materialReady = createNotificationTypes("Material bereit", "Material ist sterilisiert", "Material bereit");
        NotificationType materialNeeded = createNotificationTypes("Material", "Material bitte bringen", "Material bitte bringen");
        notificationTypeRepository.saveAll(Set.of(alarm, nextPatient, materialNeeded, materialReady));

        RuleParameters alarmRule = createRuleParameters(RuleType.NOTIFICATION_TYPE, alarm.getId().toString());
        ruleParametersRepository.saveAll(Set.of(alarmRule));

        Client empfang = createClient("Empfang");
        Client behandlungszimmer = createClient("Behandlungszimmer 1");
        Client steri = createClient("Steri");
        clientRepository.saveAll(Set.of(empfang, behandlungszimmer, steri));

        ClientConfiguration empfangConfig = createClientConfiguration(empfang, Set.of(alarm, nextPatient), Set.of(alarmRule));
        ClientConfiguration behandlungszimmerConfig = createClientConfiguration(behandlungszimmer, Set.of(alarm, materialNeeded), Set.of(alarmRule));
        ClientConfiguration steriConfig = createClientConfiguration(steri, Set.of(alarm, materialReady), Set.of(alarmRule));
        clientConfigurationRepository.saveAll(Set.of(empfangConfig, behandlungszimmerConfig, steriConfig));
    }

    private void reset() {
        callTypeRepository.deleteAll();
        notificationTypeRepository.deleteAll();
        clientConfigurationRepository.deleteAll();
        clientRepository.deleteAll();
    }

    private NotificationType createNotificationTypes(String title, String body, String display) {
        return NotificationType.builder()
                .id(UUID.randomUUID())
                .title(title)
                .body(body)
                .displayText(display)
                .clientConfigurations(Collections.emptySet())
                .build();
    }

    private RuleParameters createRuleParameters(RuleType type, String value) {
        return RuleParameters.builder()
                .type(type)
                .value(value)
                .build();
    }

    private Client createClient(String name) {
        return Client.builder()
                .name(name)
                .userId(USER_ID)
                .build();
    }

    public ClientConfiguration createClientConfiguration(Client client, Set<NotificationType> notificationTypes, Set<RuleParameters> rules) {
        return ClientConfiguration.builder()
                .clientConfigurationId(randomUUID())
                .name(client.getName())
                .client(client)
                .notificationTypes(notificationTypes)
                .rules(rules)
                .build();
    }

}
