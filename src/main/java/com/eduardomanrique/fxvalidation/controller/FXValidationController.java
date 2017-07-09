package com.eduardomanrique.fxvalidation.controller;

import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.eduardomanrique.fxvalidation.service.FXValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
public class FXValidationController {

    @Autowired
    private FXValidationService validationService;

    @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public DeferredResult<Validation> validateTransaction(@RequestBody FXTransaction transaction) {

        DeferredResult<Validation> deferredResult = new DeferredResult<>();
        validationService.validateTransaction(Collections.singletonList(transaction),
                list -> deferredResult.setResult(list.get(0)), deferredResult::setErrorResult);

        return deferredResult;
    }

    @RequestMapping(value = "/bulk-validate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public DeferredResult<List<? extends Validation>> validateTransactions(@RequestBody List<FXTransaction> transaction) {

        DeferredResult<List<? extends Validation>> deferredResult = new DeferredResult<>();
        validationService.validateTransaction(transaction, deferredResult::setResult, deferredResult::setErrorResult);

        return deferredResult;
    }
}