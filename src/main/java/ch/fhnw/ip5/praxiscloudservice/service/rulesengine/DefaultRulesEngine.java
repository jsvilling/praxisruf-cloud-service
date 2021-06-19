package ch.fhnw.ip5.praxiscloudservice.service.rulesengine;

import ch.fhnw.ip5.praxiscloudservice.api.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisNotification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultRulesEngine implements RulesEngine {

    private final RuleEvaluatorFactory ruleEvaluatorFactory;

    @Override
    public Collection<RuleParameters> filterRelevant(Collection<RuleParameters> p, PraxisNotification notification) {
        return p.stream().filter(r -> isRelevant(notification, r)).collect(Collectors.toSet());
    }

    @Override
    public boolean isAnyRelevant(Collection<RuleParameters> p, PraxisNotification notification) {
        return p.stream().anyMatch(r -> isRelevant(notification, r));
    }

    private boolean isRelevant(PraxisNotification notification, RuleParameters ruleParameters) {
        final RuleEvaluator ruleEvaluator = ruleEvaluatorFactory.get(ruleParameters.getType());
        return ruleEvaluator != null && ruleEvaluator.isRelevant(notification, ruleParameters);
    }
}
