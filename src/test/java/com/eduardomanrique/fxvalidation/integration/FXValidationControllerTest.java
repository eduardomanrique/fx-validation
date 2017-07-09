package com.eduardomanrique.fxvalidation.integration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.eduardomanrique.fxvalidation.config.IntegrationTest;

public class FXValidationControllerTest extends IntegrationTest {

    @Test
    public void pingTest() throws Exception {
        this.mvc.perform(
                MockMvcRequestBuilders
                        .post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));

    }
}
