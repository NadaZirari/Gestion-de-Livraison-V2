package com.delivrey.service;

import com.delivrey.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CustomerService {
    @NonNull
    Page<Customer> findAll(@NonNull Pageable pageable);
    
    @NonNull
    Optional<Customer> findById(@NonNull Long id);
    
    @NonNull
    Customer save(@NonNull Customer customer);
    
    @NonNull
    Customer update(@NonNull Long id, @NonNull Customer customer);
    
    void deleteById(@NonNull Long id);
    
    @NonNull
    Page<Customer> search(String name, String address, @NonNull Pageable pageable);
}
