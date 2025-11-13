package com.delivrey.service;

import com.delivrey.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CustomerService {
    @NonNull
    Page<CustomerDto> findAll(@NonNull Pageable pageable);
    
    @NonNull
    Optional<CustomerDto> findById(@NonNull Long id);
    
    @NonNull
    CustomerDto save(@NonNull CustomerDto customerDto);
    
    @NonNull
    CustomerDto update(@NonNull Long id, @NonNull CustomerDto customerDto);
    
    void deleteById(@NonNull Long id);
    
    @NonNull
    Page<CustomerDto> search(String name, String address, @NonNull Pageable pageable);
}
