package ch.fhnw.ip5.praxiscloudservice.service.rulesengine.evaluators;

import ch.fhnw.ip5.praxiscloudservice.api.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleType;
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
