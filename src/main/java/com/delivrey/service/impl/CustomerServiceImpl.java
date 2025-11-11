package com.delivrey.service.impl;

import com.delivrey.entity.Customer;
import com.delivrey.exception.NotFoundException;
import com.delivrey.repository.CustomerRepository;
import com.delivrey.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<Customer> findAll(@NonNull Pageable pageable) {
        log.debug("Fetching all customers with pagination: {}", pageable);
        return customerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<Customer> findById(@NonNull Long id) {
        log.debug("Fetching customer with id: {}", id);
        return customerRepository.findById(id);
    }

    @Override
    @Transactional
    @NonNull
    public Customer save(@NonNull Customer customer) {
        log.debug("Saving new customer: {}", customer);
        Customer savedCustomer = customerRepository.save(customer);
        if (savedCustomer == null) {
            throw new IllegalStateException("Failed to save customer: returned null");
        }
        return savedCustomer;
    }

    @Override
    @Transactional
    @NonNull
    public Customer update(@NonNull Long id, @NonNull Customer customerDetails) {
        log.debug("Updating customer with id {}: {}", id, customerDetails);
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customerDetails.getName());
                    existingCustomer.setAddress(customerDetails.getAddress());
                    existingCustomer.setLatitude(customerDetails.getLatitude());
                    existingCustomer.setLongitude(customerDetails.getLongitude());
                    existingCustomer.setPreferredTimeSlot(customerDetails.getPreferredTimeSlot());
                    return customerRepository.save(existingCustomer);
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
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<Customer> search(String name, String address, @NonNull Pageable pageable) {
        log.debug("Searching customers with name: {}, address: {}", name, address);
        if (name != null && address != null) {
            return customerRepository.findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(name, address, pageable);
        } else if (name != null) {
            return customerRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (address != null) {
            return customerRepository.findByAddressContainingIgnoreCase(address, pageable);
        }
        return customerRepository.findAll(pageable);
    }
}
