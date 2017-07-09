package com.eduardomanrique.fxvalidation.repository;

import com.eduardomanrique.fxvalidation.entity.Customer;
import org.springframework.data.repository.Repository;

public interface CustomerRepository extends Repository<Customer, Long> {

    Customer getByName(String name);
}
