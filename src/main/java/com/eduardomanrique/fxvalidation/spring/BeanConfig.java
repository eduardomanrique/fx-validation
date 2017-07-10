package com.eduardomanrique.fxvalidation.spring;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
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

    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    @Bean
    public EmbeddedServletContainerCustomizer tomcatCustomizer() {
        return container -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                ((TomcatEmbeddedServletContainerFactory) container)
                        .addConnectorCustomizers(gracefulShutdown());
            }
        };
    }

    private static class GracefulShutdown implements TomcatConnectorCustomizer,
            ApplicationListener<ContextClosedEvent> {

        private volatile Connector connector;

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            this.connector.pause();
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                        log.warn("Tomcat thread pool did not shut down gracefully within "
                                + "30 seconds. Proceeding with forceful shutdown");
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
}
