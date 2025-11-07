package com.delivrey.repository;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    
    // Basic queries
    List<Delivery> findByStatus(DeliveryStatus status);
    
    // Find deliveries by customer
    List<Delivery> findByCustomerId(Long customerId);
    
    // Find deliveries with pagination
    Page<Delivery> findByStatus(DeliveryStatus status, Pageable pageable);
    
    // Find deliveries within a time window
    List<Delivery> findByTimeWindow(String timeWindow);
    
    // Find deliveries by customer and status
    List<Delivery> findByCustomerIdAndStatus(Long customerId, DeliveryStatus status);
    
    // Find deliveries with customer details (eager loading)
    @Query("""
        SELECT d FROM Delivery d 
        LEFT JOIN FETCH d.customer c 
        WHERE d.id = :id
    """)
    Optional<Delivery> findByIdWithCustomer(@Param("id") Long id);
    
    // Find deliveries near a location (within radius in km)
    @Query("""
        SELECT d FROM Delivery d 
        WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(d.latitude)) * 
              cos(radians(d.longitude) - radians(:longitude)) + 
              sin(radians(:latitude)) * sin(radians(d.latitude)))) <= :radiusKm
        AND d.status = :status
    """)
    List<Delivery> findNearbyDeliveries(
        @Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("radiusKm") double radiusKm,
        @Param("status") DeliveryStatus status
    );
    
    // Count deliveries by status
    long countByStatus(DeliveryStatus status);
    
    // Find deliveries created between dates
    List<Delivery> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find deliveries by weight range
    List<Delivery> findByWeightBetween(Double minWeight, Double maxWeight);
    
    // Find deliveries by volume range
    List<Delivery> findByVolumeBetween(Double minVolume, Double maxVolume);
}
