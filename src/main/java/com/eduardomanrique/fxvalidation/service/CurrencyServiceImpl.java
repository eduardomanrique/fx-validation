package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.entity.Currency;
import com.eduardomanrique.fxvalidation.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository repository;

    @Override
    @Cacheable("currency")
    public Optional<Currency> getCurrencyByIsoCode(String isoCode) {
        return Optional.ofNullable(repository.getByIsoCode(isoCode));
    }
}
