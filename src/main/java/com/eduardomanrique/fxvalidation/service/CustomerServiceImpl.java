package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.entity.Customer;
import com.eduardomanrique.fxvalidation.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Override
    @Cacheable("customer")
    public Optional<Customer> getCustomerByName(String name) {
        return Optional.ofNullable(repository.getByName(name));
    }
}
