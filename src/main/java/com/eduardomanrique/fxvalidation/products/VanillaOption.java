package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public abstract class VanillaOption extends FXTransaction {
    private String strategy;
    private Date deliveryDate;
    private Date expiryDate;
    @JsonIgnore
    private Currency payCurrency;
    private String payCcy;
    private BigDecimal premium;
    @JsonIgnore
    private Currency premiumCurrency;
    private String premiumCcy;
    private String premiumType;
    private Date premiumDate;
}
