package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.rulesengine.RuleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FXValidationServiceImpl implements FXValidationService {

    @Autowired
    private RuleEngine ruleEngine;

    private int processors = Runtime.getRuntime().availableProcessors();

    @Override
    public void validateTransaction(List<FXTransaction> transactionList, Consumer<List<FXValidationResult>> callback, Consumer<Throwable> onError) {
        CompletableFuture.runAsync(() -> {
            try {
                callback.accept(new ForkJoinPool(processors).submit(() ->
                        transactionList.parallelStream()
                                .map(this::execRules)
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList())
                ).get());
            } catch (Throwable t) {
                onError.accept(t);
            }
        });
    }

    private CompletableFuture<FXValidationResult> execRules(FXTransaction transaction) {
        return CompletableFuture.supplyAsync(() -> {

            FXValidationResult validationResult = new FXValidationResult(
                    transaction.getCustomer().getName(),
                    transaction.getCurrencyPair().getCurrency1().getIsoCode() +
                            transaction.getCurrencyPair().getCurrency2().getIsoCode(),
                    transaction.getClass().getSimpleName(),
                    transaction.getDirection().name(),
                    transaction.getTradeDate());

            ruleEngine.fireRules(transaction, validationResult);
            return validationResult;
        });
    }
}
