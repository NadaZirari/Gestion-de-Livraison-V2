package com.delivrey.dao;

import com.delivrey.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> findAll();
    Page<Customer> findAll(Pageable pageable);
    Optional<Customer> findById(Long id);
    Customer save(Customer customer);
    void deleteById(Long id);
    List<Customer> findByNameContainingIgnoreCase(String name);
    Page<Customer> findByAddressContainingIgnoreCase(String address, Pageable pageable);
    Page<Customer> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);
    Page<Customer> findByFirstNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
        String firstName, String address, Pageable pageable);
    List<Customer> findByPreferredTimeSlot(String timeSlot);
    Optional<Customer> findByNameIgnoreCase(String name);
    boolean existsById(Long id);
}
