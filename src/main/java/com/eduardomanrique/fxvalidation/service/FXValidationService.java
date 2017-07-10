package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public interface FXValidationService {
    List<FXValidationResult> validateTransaction(List<FXTransaction> transactionList);

    class FXValidationResult extends Validation {

        @Getter
        private final String customerName;
        @Getter
        private final String currencyPair;
        @Getter
        private final String type;
        @Getter
        private final String direction;
        @Getter
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private final Date tradeDate;

        public FXValidationResult(String customerName, String currencyPair, String type, String direction, Date tradeDate) {
            this.customerName = customerName;
            this.currencyPair = currencyPair;
            this.type = type;
            this.direction = direction;
            this.tradeDate = tradeDate;
        }
    }
}
