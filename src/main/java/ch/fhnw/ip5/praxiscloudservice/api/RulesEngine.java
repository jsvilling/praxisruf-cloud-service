package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;

import java.util.Collection;

/**
 * This interface specifies how a collection of RuleParameters can be applied to a PraxisNotification.
 *
 * This can be used to determine whether or not a PraxisNotification is relevant for a client that was
 * configured with the given RuleParameter collection.
 */
public interface RulesEngine {

    /**
     * Evaluates all given RuleParameters for the given PraxisNotification.
     *
     * The actual evaluation of the RuleParameters is delegated to the respective RuleEvaluator instances.
     *
     * @param ruleParameters
     * @param notification
     * @return
     */
    boolean isAnyRelevant(Collection<RuleParameters> ruleParameters, PraxisNotification notification);

}
