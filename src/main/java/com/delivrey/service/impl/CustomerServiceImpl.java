package com.delivrey.service.impl;

import com.delivrey.dto.CustomerDto;
import com.delivrey.entity.Customer;
import com.delivrey.exception.NotFoundException;
import com.delivrey.mapper.CustomerMapper;
import com.delivrey.repository.CustomerRepository;
import com.delivrey.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("null") // Suppression des avertissements de nullité pour ce fichier
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<CustomerDto> findAll(@NonNull Pageable pageable) {
        log.debug("Fetching all customers with pagination: {}", pageable);
        Page<Customer> customers = customerRepository.findAll(pageable);
        List<CustomerDto> dtos = customers.getContent().stream()
                .map(customerMapper::toDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, customers.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<CustomerDto> findById(@NonNull Long id) {
        log.debug("Fetching customer with id: {}", id);
        return customerRepository.findById(id)
                .map(customer -> {
                    CustomerDto dto = customerMapper.toDto(customer);
                    if (dto == null) {
                        throw new IllegalStateException("Mapping to DTO returned null for customer id: " + id);
                    }
                    return dto;
                });
    }

    @Override
    @Transactional
    @NonNull
    public CustomerDto save(@NonNull CustomerDto customerDto) {
        log.debug("Saving new customer: {}", customerDto);
        Customer customer = customerMapper.toEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        if (savedCustomer == null) {
            throw new IllegalStateException("Failed to save customer: returned null");
        }
        CustomerDto result = customerMapper.toDto(savedCustomer);
        if (result == null) {
            throw new IllegalStateException("Mapping to DTO returned null");
        }
        return result;
    }

    @Override
    @Transactional
    @NonNull
    public CustomerDto update(@NonNull Long id, @NonNull CustomerDto customerDto) {
        log.debug("Updating customer with id {}: {}", id, customerDto);
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    customerMapper.updateCustomerFromDto(customerDto, existingCustomer);
                    Customer updatedCustomer = customerRepository.save(existingCustomer);
                    log.debug("Updated customer: {}", updatedCustomer);
                    CustomerDto result = customerMapper.toDto(updatedCustomer);
                    if (result == null) {
                        throw new IllegalStateException("Mapping to DTO returned null");
                    }
                    return result;
                })
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        log.debug("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
        log.debug("Successfully deleted customer with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<CustomerDto> search(String name, String address, @NonNull Pageable pageable) {
        log.debug("Searching customers with name: {}, address: {}", name, address);
        Page<Customer> customers;
        if (name != null && address != null) {
            customers = customerRepository.findByFirstNameContainingIgnoreCaseAndAddressContainingIgnoreCase(name, address, pageable);
        } else if (name != null) {
            customers = customerRepository.findByFirstNameContainingIgnoreCase(name, pageable);
        } else if (address != null) {
            customers = customerRepository.findByAddressContainingIgnoreCase(address, pageable);
        } else {
            customers = customerRepository.findAll(pageable);
        }
        
        List<CustomerDto> dtos = customers.getContent().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
                
        return new PageImpl<>(dtos, pageable, customers.getTotalElements());
    }
}
