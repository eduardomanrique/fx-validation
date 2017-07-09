package com.eduardomanrique.fxvalidation.config;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class BeanConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        //Preparing derby db
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("sql/create-db.sql")
                .addScript("sql/insert-data.sql")
                .build();
        return new JdbcTemplate(db);
    }

    @Bean
    public HandlerInstantiator handlerInstantiator() {
        return new SpringHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory());
    }

}
