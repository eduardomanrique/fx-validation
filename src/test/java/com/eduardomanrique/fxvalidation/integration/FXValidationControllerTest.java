package com.eduardomanrique.fxvalidation.integration;

import com.eduardomanrique.fxvalidation.service.FixerIOApiGatewayService;
import com.eduardomanrique.fxvalidation.spring.BeanConfig;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BeanConfig.class, TestConfig.class})
@ComponentScan("com.eduardomanrique.fxvalidation")
@AutoConfigureMockMvc
@ActiveProfiles("integratied_test")
public class FXValidationControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    FixerIOApiGatewayService fixerIOApiGatewayService;

    @Before
    public void before() {
        Mockito.when(fixerIOApiGatewayService.isBusinessDay(Mockito.any())).thenReturn(true);
        Mockito.when(fixerIOApiGatewayService.businessDaysBetween(Mockito.any(), Mockito.any())).thenReturn(2);
    }

    @Test
    public void validateTest() throws Exception {
        String payload = IOUtils.toString(
                FXValidationControllerTest.class.getResourceAsStream("/validateTest.json"), "UTF-8");
        String response = IOUtils.toString(
                FXValidationControllerTest.class.getResourceAsStream("/responseValidateTest.json"), "UTF-8");

        this.mvc.perform(
                MockMvcRequestBuilders
                        .post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(response));
    }

    @Test
    public void bulkValidateTest() throws Exception {
        String payload = IOUtils.toString(
                FXValidationControllerTest.class.getResourceAsStream("/bulkValidateTest.json"), "UTF-8");
        String response = IOUtils.toString(
                FXValidationControllerTest.class.getResourceAsStream("/responseBulkValidateTest.json"), "UTF-8");

        this.mvc.perform(
                MockMvcRequestBuilders
                        .post("/bulk-validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(response));

    }
}
