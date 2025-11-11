package com.delivrey.repository;

import com.delivrey.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Find customer by exact name (case-insensitive)
    @NonNull
    Optional<Customer> findByNameIgnoreCase(@NonNull String name);
    
    // Search customers by name containing (case-insensitive)
    @NonNull
    List<Customer> findByNameContainingIgnoreCase(@NonNull String name);
    
    // Search customers by name containing (case-insensitive) with pagination
    @NonNull
    Page<Customer> findByNameContainingIgnoreCase(@NonNull String name, @NonNull Pageable pageable);
    
    // Search customers by address containing (case-insensitive) with pagination
    @NonNull
    Page<Customer> findByAddressContainingIgnoreCase(@NonNull String address, @NonNull Pageable pageable);
    
    // Search customers by name and address containing (case-insensitive) with pagination
    @NonNull
    Page<Customer> findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
        @Nullable String name, @Nullable String address, @NonNull Pageable pageable);
        
    @Override
    @NonNull
    <S extends Customer> S save(@NonNull S entity);
    
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
        @Param("name") @NonNull String name, 
        @Param("address") @NonNull String address, 
        @NonNull Pageable pageable
    );
}
