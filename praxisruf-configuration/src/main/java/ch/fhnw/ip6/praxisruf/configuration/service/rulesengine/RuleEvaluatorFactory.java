package ch.fhnw.ip6.praxisruf.configuration.service.rulesengine;

import ch.fhnw.ip6.praxisruf.configuration.api.rulesengine.RuleEvaluator;
import ch.fhnw.ip6.praxisruf.configuration.domain.RuleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleEvaluatorFactory {

    private final Map<RuleType, RuleEvaluator> evaluators = new HashMap<>();

    @Autowired
    public RuleEvaluatorFactory(List<RuleEvaluator> evaluatorInstances) {
        evaluatorInstances.forEach(e -> evaluators.put(e.getRelevantType(), e));
    }

    public RuleEvaluator get(RuleType ruleType) {
        return evaluators.get(ruleType);
    }

}
