package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.Notification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleType;

public interface RuleEvaluator {

    RuleType getRelevantType();

    boolean isRelevant(Notification notification, RuleParameters ruleParameters);

}
