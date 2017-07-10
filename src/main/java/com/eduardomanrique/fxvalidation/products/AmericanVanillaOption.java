package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AmericanVanillaOption extends VanillaOption {
    private Date excerciseStartDate;
}
