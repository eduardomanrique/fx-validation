package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.RuleEngine;
import com.eduardomanrique.fxvalidation.service.FixerIOApiGatewayService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class TestConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public FixerIOApiGatewayService fixerIOApiGatewayService() {
        return Mockito.mock(FixerIOApiGatewayService.class);
    }

    @Bean
    public RuleEngine ruleEngine() {
        List<Rule> list = Arrays.asList(new AllTradesRule(), new AllOptionsRule(), new SpotRule(), new ForwardRule(), new AmericanOptionsRule());
        list.stream().forEach(applicationContext.getAutowireCapableBeanFactory()::autowireBean);
        return new RuleEngine(list);
    }
}
