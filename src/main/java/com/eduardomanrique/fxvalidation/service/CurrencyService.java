package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.entity.Currency;

import java.util.Optional;

public interface CurrencyService {
    Optional<Currency> getCurrencyByIsoCode(String isoCode);
}
