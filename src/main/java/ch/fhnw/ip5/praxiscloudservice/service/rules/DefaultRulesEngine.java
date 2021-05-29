package ch.fhnw.ip5.praxiscloudservice.service.rules;

import ch.fhnw.ip5.praxiscloudservice.api.RuleEvaluator;
import ch.fhnw.ip5.praxiscloudservice.api.RulesEngine;
import ch.fhnw.ip5.praxiscloudservice.domain.Notification;
import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import ch.fhnw.ip5.praxiscloudservice.persistence.RuleParametersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultRulesEngine implements RulesEngine {

    private final RuleParametersRepository ruleParametersRepository;
    private final RuleEvaluatorFactory ruleEvaluatorFactory;

    @Override
    public boolean isAnyRelevant(Notification notification) {
        return ruleParametersRepository.findAll()
                .stream()
                .anyMatch(p -> isRelevant(notification, p));
    }

    private boolean isRelevant(Notification notification, RuleParameters ruleParameters) {
        final RuleEvaluator ruleEvaluator = ruleEvaluatorFactory.get(ruleParameters.getType());
        return ruleEvaluator != null && ruleEvaluator.isRelevant(notification, ruleParameters);
    }
}
