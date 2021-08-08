package ch.fhnw.ip5.praxiscloudservice.service.configuration.rulesengine.evaluators;

import ch.fhnw.ip5.praxiscloudservice.api.configuration.rulesengine.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleType;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationTypeRuleEvaluator implements RuleEvaluator {

    @Override
    public RuleType getRelevantType() {
        return RuleType.NOTIFICATION_TYPE;
    }

    @Override
    public boolean isRelevant(PraxisNotification notification, RuleParameters ruleParameters) {
        if (isInvalidRequest(notification, ruleParameters)) {
            return false;
        }
        final UUID relevantNotificationTypeId = UUID.fromString(ruleParameters.getValue());
        return relevantNotificationTypeId.equals(notification.getNotificationTypeId());
    }

    private boolean isInvalidRequest(PraxisNotification notification, RuleParameters ruleParameters) {
        return notification == null
                || ruleParameters == null
                || !getRelevantType().equals(ruleParameters.getType());
    }
}
