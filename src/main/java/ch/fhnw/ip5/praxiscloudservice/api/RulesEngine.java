package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.Notification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;

public interface RulesEngine {

    boolean isAnyRelevant(Notification notification);

}
