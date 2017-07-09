package com.eduardomanrique.fxvalidation.config;

import java.util.TimeZone;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
public abstract class BaseTest {

    @Before
    public void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
