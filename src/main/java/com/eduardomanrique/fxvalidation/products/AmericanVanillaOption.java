package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AmericanVanillaOption extends VanillaOption {
    private Date excerciseStartDate;
}
