package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.products.Spot;
import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.RuleEngine;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test_service")
public class TestConfig {

    @Bean
    public RuleEngine ruleEngine() {
        return new RuleEngine(Arrays.asList(
                new Rule<Object>() {
                    public int getPriotiy() {
                        return 0;
                    }

                    public void onFact(Object fact, Validation validation) {
                        if (fact instanceof Spot) {
                            validation.addValidationError("SpotRule", "...");
                        } else {
                            validation.addValidationError("NonSpotRule", "...");
                        }
                    }
                }));
    }
}
