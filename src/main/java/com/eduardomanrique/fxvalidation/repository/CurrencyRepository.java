package com.eduardomanrique.fxvalidation.repository;

import com.eduardomanrique.fxvalidation.entity.Currency;
import com.eduardomanrique.fxvalidation.entity.Customer;
import org.springframework.data.repository.Repository;

public interface CurrencyRepository extends Repository<Currency, String> {

    Currency getByIsoCode(String name);
}
