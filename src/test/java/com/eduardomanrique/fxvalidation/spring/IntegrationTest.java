package com.eduardomanrique.fxvalidation.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class IntegrationTest extends BaseTest {

    @Autowired
    protected MockMvc mvc;

}
