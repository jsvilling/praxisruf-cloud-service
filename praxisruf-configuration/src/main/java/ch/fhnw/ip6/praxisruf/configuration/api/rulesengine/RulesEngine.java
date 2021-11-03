package ch.fhnw.ip6.praxisruf.configuration.api.rulesengine;

import ch.fhnw.ip6.praxisruf.commons.dto.notification.SendPraxisNotificationDto;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleParameters;

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
    boolean isAnyRelevant(Collection<RuleParameters> ruleParameters, SendPraxisNotificationDto notification);

}
