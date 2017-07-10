package com.eduardomanrique.fxvalidation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@Slf4j
public class FXValidationApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Exiting application...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            log.info("Graceful shutdown");
        }));
        SpringApplication.run(FXValidationApiApplication.class, args);
    }

}
