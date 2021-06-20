package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleType;

public interface RuleEvaluator {

    RuleType getRelevantType();

    boolean isRelevant(PraxisNotification notification, RuleParameters ruleParameters);

}
