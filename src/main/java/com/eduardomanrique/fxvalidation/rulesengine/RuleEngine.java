package com.eduardomanrique.fxvalidation.rulesengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
@Slf4j
public class RuleEngine {

    @Autowired
    private List<Rule> ruleList;

    private SortedSet<Rule> sortedRuleSet = new TreeSet<>((o1, o2) -> Integer.compare(o1.getPriotiy(), o2.getPriotiy()));

    @PostConstruct
    public final void init() {
        sortedRuleSet.addAll(ruleList);
    }

    public void fireRules(final Object fact, final Validation validation) {
        sortedRuleSet.stream()
                .filter(rule -> filterByParameterType(rule, fact))
                .forEach(rule -> rule.onFact(fact, validation));
    }

    private boolean filterByParameterType(Rule rule, Object fact) {
        return ((Class) ((ParameterizedType) rule.getClass().getGenericInterfaces()[0])
                .getActualTypeArguments()[0]).isAssignableFrom(fact.getClass());
    }

}
