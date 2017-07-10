package com.eduardomanrique.fxvalidation.controller;

import com.eduardomanrique.fxvalidation.service.FXValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@Slf4j
public class MetricsController {

    @Autowired
    private FXValidationService validationService;

    @RequestMapping(value = "/metrics", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public void getMetrics(HttpServletResponse response) throws IOException {
        FXValidationService.Metrics metrics = validationService.getMetrics();
        PrintWriter writer = response.getWriter();
        writer.write("Total requests: " + metrics.getTotalRequests() + "\n");
        writer.write("Average time:   " + metrics.getAverage() + "\n");
        writer.write("Max time:       " + metrics.getMax() + "\n");
        writer.write("Min time:       " + metrics.getMin() + "\n");
        writer.write("Quantile 95:    " + metrics.calculateQuantile95() + "\n");
    }

}