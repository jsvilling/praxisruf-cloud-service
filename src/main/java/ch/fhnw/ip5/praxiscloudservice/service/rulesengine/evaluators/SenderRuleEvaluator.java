package ch.fhnw.ip5.praxiscloudservice.service.rulesengine.evaluators;

import ch.fhnw.ip5.praxiscloudservice.api.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleType;
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
