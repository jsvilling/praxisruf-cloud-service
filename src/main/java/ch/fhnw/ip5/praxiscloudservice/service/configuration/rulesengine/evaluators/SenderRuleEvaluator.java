package ch.fhnw.ip5.praxiscloudservice.service.configuration.rulesengine.evaluators;

import ch.fhnw.ip5.praxiscloudservice.api.configuration.rulesengine.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleType;
import ch.fhnw.ip5.praxiscloudservice.domain.notification.PraxisNotification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SenderRuleEvaluator implements RuleEvaluator {

    @Override
    public RuleType getRelevantType() {
        return RuleType.SENDER;
    }

    @Override
    public boolean isRelevant(PraxisNotification notification, RuleParameters ruleParameters) {
        if (isInvalidRequest(notification, ruleParameters)) {
            return false;
        }
        final UUID senderIdParam = UUID.fromString(ruleParameters.getValue());
        return notification.getSender().equals(senderIdParam);
    }

    private boolean isInvalidRequest(PraxisNotification notification, RuleParameters ruleParameters) {
        return notification == null
                || ruleParameters == null
                || !getRelevantType().equals(ruleParameters.getType());
    }
}
