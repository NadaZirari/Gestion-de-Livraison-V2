package com.delivrey.service.impl;

import com.delivrey.dao.CustomerDao;
import com.delivrey.dto.CustomerDto;
import com.delivrey.entity.Customer;
import com.delivrey.exception.NotFoundException;
import com.delivrey.mapper.CustomerMapper;
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

    private final CustomerDao customerDao;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<CustomerDto> findAll(@NonNull Pageable pageable) {
        log.debug("Fetching all customers with pagination: {}", pageable);
        Page<Customer> customers = customerDao.findAll(pageable);
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
        return customerDao.findById(id)
                .map(customerMapper::toDto);
    }

    @Override
    @Transactional
    @NonNull
    public CustomerDto save(@NonNull CustomerDto customerDto) {
        log.debug("Saving customer: {}", customerDto);
        Customer customer = customerMapper.toEntity(customerDto);
        Customer savedCustomer = customerDao.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional
    @NonNull
    public CustomerDto update(@NonNull Long id, @NonNull CustomerDto customerDto) {
        log.debug("Updating customer with id {}: {}", id, customerDto);
        return customerDao.findById(id)
                .map(existingCustomer -> {
                    customerMapper.updateCustomerFromDto(customerDto, existingCustomer);
                    Customer updatedCustomer = customerDao.save(existingCustomer);
                    return customerMapper.toDto(updatedCustomer);
                })
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        log.debug("Deleting customer with id: {}", id);
        if (!customerDao.existsById(id)) {
            throw new NotFoundException("Customer not found with id: " + id);
        }
        customerDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<CustomerDto> search(String name, String address, @NonNull Pageable pageable) {
        log.debug("Searching customers with name: {}, address: {}", name, address);
        Page<Customer> customers;
        if (name != null && address != null) {
            customers = customerDao.findByFirstNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
                name, address, pageable);
        } else if (name != null) {
            customers = customerDao.findByFirstNameContainingIgnoreCase(name, pageable);
        } else if (address != null) {
            customers = customerDao.findByAddressContainingIgnoreCase(address, pageable);
        } else {
            customers = customerDao.findAll(pageable);
        }
        
        List<CustomerDto> dtos = customers.getContent().stream()
                .map(customerMapper::toDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
                
        return new PageImpl<>(dtos, pageable, customers.getTotalElements());
    }
}
