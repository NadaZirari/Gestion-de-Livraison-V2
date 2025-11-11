package com.delivrey.repository;

import com.delivrey.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Find customer by exact name (case-insensitive)
    Optional<Customer> findByNameIgnoreCase(String name);
    
    // Search customers by name containing (case-insensitive)
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    // Search customers by name containing (case-insensitive) with pagination
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Search customers by address containing (case-insensitive) with pagination
    Page<Customer> findByAddressContainingIgnoreCase(String address, Pageable pageable);
    
    // Search customers by name and address containing (case-insensitive) with pagination
    Page<Customer> findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
        String name, String address, Pageable pageable);
    
    // Find customers by their preferred time slot
    List<Customer> findByPreferredTimeSlot(String timeSlot);
    
    // Find customers near a specific location (within a radius in kilometers)
    @Query("""
        SELECT c FROM Customer c 
        WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * 
              cos(radians(c.longitude) - radians(:longitude)) + 
              sin(radians(:latitude)) * sin(radians(c.latitude)))) <= :radiusKm
    """)
    List<Customer> findNearbyCustomers(
        @Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("radiusKm") double radiusKm
    );
    
    // Find customers near a specific location with pagination
    @Query("""
        SELECT c FROM Customer c 
        WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * 
              cos(radians(c.longitude) - radians(:longitude)) + 
              sin(radians(:latitude)) * sin(radians(c.latitude)))) <= :radiusKm
    """)
    Page<Customer> findNearbyCustomers(
        @Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("radiusKm") double radiusKm,
        Pageable pageable
    );
}
