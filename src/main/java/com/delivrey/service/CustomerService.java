package com.delivrey.service;

import com.delivrey.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerService {
    Page<Customer> findAll(Pageable pageable);
    Optional<Customer> findById(Long id);
    Customer save(Customer customer);
    Customer update(Long id, Customer customer);
    void deleteById(Long id);
    Page<Customer> search(String name, String address, Pageable pageable);
}
