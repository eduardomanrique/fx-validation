package com.eduardomanrique.fxvalidation.products;

import com.eduardomanrique.fxvalidation.entity.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@Data
@EqualsAndHashCode
public class CurrencyPair {
    private Optional<Currency> currency1;
    private Optional<Currency> currency2;
}
