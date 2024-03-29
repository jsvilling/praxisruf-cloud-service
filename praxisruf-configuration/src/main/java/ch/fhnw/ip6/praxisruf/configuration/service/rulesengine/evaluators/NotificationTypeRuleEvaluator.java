package ch.fhnw.ip6.praxisruf.configuration.service.rulesengine.evaluators;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.configuration.api.rulesengine.RuleEvaluator;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleParameters;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationTypeRuleEvaluator implements RuleEvaluator {

    @Override
    public RuleType getRelevantType() {
        return RuleType.NOTIFICATION_TYPE;
    }

    @Override
    public boolean isRelevant(SendPraxisNotificationDto notification, RuleParameters ruleParameters) {
        if (isInvalidRequest(notification, ruleParameters)) {
            return false;
        }
        final UUID relevantNotificationTypeId = UUID.fromString(ruleParameters.getValue());
        return relevantNotificationTypeId.equals(notification.getNotificationTypeId());
    }

    private boolean isInvalidRequest(SendPraxisNotificationDto notification, RuleParameters ruleParameters) {
        return notification == null
                || ruleParameters == null
                || !getRelevantType().equals(ruleParameters.getType());
    }
}
