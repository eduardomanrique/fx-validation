package com.eduardomanrique.fxvalidation.spring;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
                .addScript("sql/drop-tables.sql")
                .addScript("sql/create-db.sql")
                .addScript("sql/insert-data.sql")
                .ignoreFailedDrops(true)
                .build();
        return new JdbcTemplate(db);
    }

    @Bean
    public HandlerInstantiator handlerInstantiator() {
        return new SpringHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory());
    }

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        CaffeineCache currencyCache = buildCache("currency", ticker, 1000);
        CaffeineCache customerCache = buildCache("customer", ticker, 2);
        CaffeineCache businessDaysCache = buildCache("businessDays", ticker, 10000);
        CaffeineCache businessDaysBetweenCache = buildCache("businessDaysBetween", ticker, 10000);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(currencyCache, customerCache, businessDaysCache, businessDaysBetweenCache));
        return manager;
    }

    private CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
                .maximumSize(100)
                .ticker(ticker)
                .build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

}
