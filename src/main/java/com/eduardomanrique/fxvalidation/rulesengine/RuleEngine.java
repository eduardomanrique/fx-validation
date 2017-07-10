package com.eduardomanrique.fxvalidation.rulesengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
@Slf4j
public class RuleEngine {

    private SortedSet<Rule> sortedRuleSet;

    @Autowired
    public RuleEngine(List<Rule> ruleList) {
        sortedRuleSet = new TreeSet<>(Comparator.comparing(Rule::getPriotiy));
        sortedRuleSet.addAll(ruleList);
        if (sortedRuleSet.size() != ruleList.size()) {
            throw new RuntimeException("Rules are possibly wrong. There are conflicting priorities");
        }
    }

    public final void fireRules(final Object fact, final Validation validation) {
        sortedRuleSet.stream()
                .filter(rule -> filterByParameterType(rule, fact))
                .forEach(rule -> rule.onFact(fact, validation));
    }

    private boolean filterByParameterType(Rule rule, Object fact) {
        return ((Class) ((ParameterizedType) rule.getClass().getGenericInterfaces()[0])
                .getActualTypeArguments()[0]).isAssignableFrom(fact.getClass());
    }

}
