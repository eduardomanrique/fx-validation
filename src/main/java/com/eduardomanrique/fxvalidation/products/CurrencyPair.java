package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CurrencyPair {
    private Currency currency1;
    private Currency currency2;
}
