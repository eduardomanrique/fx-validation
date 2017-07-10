package com.eduardomanrique.fxvalidation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableCaching
@Slf4j
public class FXValidationApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FXValidationApiApplication.class, args);
    }

    @PreDestroy
    public void onExit() {
        log.info("Exiting application");
    }

}
