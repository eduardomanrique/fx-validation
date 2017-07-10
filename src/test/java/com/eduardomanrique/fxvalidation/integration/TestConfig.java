package com.eduardomanrique.fxvalidation.integration;

import com.eduardomanrique.fxvalidation.service.FixerIOApiGatewayService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("integratied_test")
public class TestConfig {

    @Bean
    public FixerIOApiGatewayService fixerIOApiGatewayService() {
        return Mockito.mock(FixerIOApiGatewayService.class);
    }

}
