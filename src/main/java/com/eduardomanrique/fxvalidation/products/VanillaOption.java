package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public abstract class VanillaOption extends FXTransaction {
    private String strategy;
    private Date deliveryDate;
    private Date expiryDate;
    private Currency payCcy;
    private BigDecimal premium;
    private Currency premiumCcy;
    private String premiumType;
    private Date premiumDate;
}
