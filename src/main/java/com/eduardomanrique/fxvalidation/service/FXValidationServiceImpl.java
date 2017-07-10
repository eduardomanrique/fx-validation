package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.entity.Currency;
import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.rulesengine.RuleEngine;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FXValidationServiceImpl implements FXValidationService {

    @Autowired
    private RuleEngine ruleEngine;

    @Getter
    private Metrics metrics = new Metrics();

    @Override
    public List<FXValidationResult> validateTransaction(List<FXTransaction> transactionList) {
        long init = System.currentTimeMillis();
        List<FXValidationResult> result = transactionList.parallelStream()
                .map(this::execRules)
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        metrics.addTime(System.currentTimeMillis() - init);
        return result;
    }

    private CompletableFuture<FXValidationResult> execRules(FXTransaction transaction) {
        return CompletableFuture.supplyAsync(() -> {

            FXValidationResult validationResult = new FXValidationResult(
                    transaction.getCustomer(),
                    transaction.getCurrencyPair().getCurrency1().map(Currency::getIsoCode).orElse(null) +
                            transaction.getCurrencyPair().getCurrency2().map(Currency::getIsoCode).orElse(null),
                    transaction.getClass().getSimpleName(),
                    transaction.getDirection().name(),
                    transaction.getTradeDate());

            ruleEngine.fireRules(transaction, validationResult);
            return validationResult;
        });
    }


}
