package com.delivrey.dao.impl;

import com.delivrey.dao.CustomerDao;
import com.delivrey.entity.Customer;
import com.delivrey.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerDaoImpl implements CustomerDao {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> findByNameContainingIgnoreCase(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Customer> findByAddressContainingIgnoreCase(String address, Pageable pageable) {
        return customerRepository.findByAddressContainingIgnoreCase(address, pageable);
    }

    @Override
    public Page<Customer> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable) {
        return customerRepository.findByFirstNameContainingIgnoreCase(firstName, pageable);
    }

    @Override
    public Page<Customer> findByFirstNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
            String firstName, String address, Pageable pageable) {
        return customerRepository.findByFirstNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
                firstName, address, pageable);
    }

    @Override
    public List<Customer> findByPreferredTimeSlot(String timeSlot) {
        return customerRepository.findByPreferredTimeSlot(timeSlot);
    }

    @Override
    public Optional<Customer> findByNameIgnoreCase(String name) {
        return customerRepository.findByNameIgnoreCase(name);
    }

    @Override
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }
}
