package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Customer;
import com.eduardomanrique.fxvalidation.products.deserializer.FXTransactionDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonDeserialize(using = FXTransactionDeserializer.class)
public abstract class FXTransaction {

    public enum Direction {
        BUY, SELL
    }

    @JsonIgnore
    private Customer customerEntity;
    private String customer;
    @JsonIgnore
    private CurrencyPair currencyPair;
    private String ccyPair;
    private Direction direction;
    private Date tradeDate;
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal rate;
    private String legalEntity;
    private String trader;
}
