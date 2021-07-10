package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleType;

/**
 * This interface specifies the evaluation of notification subscription rules.
 *
 * There has to be exactly one RuleEvaluator instance for each {@link RuleType}.
 *
 * Each instance is responsible for evaluating whether a given PraxisNotification is relevant for
 * its RuleType considering the given RuleParameters.
 */
public interface RuleEvaluator {

    /**
     * @return RuleType relevant for this RuleEvaluator
     */
    RuleType getRelevantType();

    /**
     *
     * Evaluates whether a given PraxisNotification is relevant for its RuleType considering the given RuleParameters.
     * @param notification
     * @param ruleParameters
     * @return boolean - The evaluation result
     */
    boolean isRelevant(PraxisNotification notification, RuleParameters ruleParameters);

}
