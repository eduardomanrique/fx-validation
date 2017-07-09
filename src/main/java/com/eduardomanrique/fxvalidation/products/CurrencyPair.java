package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Currency;
import lombok.Data;

@Data
public class CurrencyPair {
    private Currency currency1;
    private Currency currency2;
}
