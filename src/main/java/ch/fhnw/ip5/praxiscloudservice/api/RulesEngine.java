package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;

import java.util.Collection;
import java.util.Set;

public interface RulesEngine {

    Collection<RuleParameters> filterRelevant(Collection<RuleParameters> p, PraxisNotification notification);

    boolean isAnyRelevant(Collection<RuleParameters> p, PraxisNotification notification);

}
