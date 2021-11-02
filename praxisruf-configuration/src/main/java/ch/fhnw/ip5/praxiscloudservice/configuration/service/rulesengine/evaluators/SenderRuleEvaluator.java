package ch.fhnw.ip5.praxiscloudservice.configuration.service.rulesengine.evaluators;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip5.praxiscloudservice.configuration.api.rulesengine.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.RuleType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SenderRuleEvaluator implements RuleEvaluator {

    @Override
    public RuleType getRelevantType() {
        return RuleType.SENDER;
    }

    @Override
    public boolean isRelevant(SendPraxisNotificationDto notification, RuleParameters ruleParameters) {
        if (isInvalidRequest(notification, ruleParameters)) {
            return false;
        }
        final UUID senderIdParam = UUID.fromString(ruleParameters.getValue());
        return notification.getSender().equals(senderIdParam);
    }

    private boolean isInvalidRequest(SendPraxisNotificationDto notification, RuleParameters ruleParameters) {
        return notification == null
                || ruleParameters == null
                || !getRelevantType().equals(ruleParameters.getType());
    }
}
