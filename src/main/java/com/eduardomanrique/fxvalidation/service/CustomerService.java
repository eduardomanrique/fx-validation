package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> getCustomerByName(String name);
}
