package com.delivrey.repository;

import com.delivrey.entity.DeliveryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
    
    // Paginated queries
    Page<DeliveryHistory> findAll(Pageable pageable);
    
    // Find all delivery histories for a specific customer with pagination
    Page<DeliveryHistory> findByCustomerId(Long customerId, Pageable pageable);
    
    // Find all delivery histories for a specific tour with pagination
    Page<DeliveryHistory> findByTourId(Long tourId, Pageable pageable);
    
    // Find delivery histories within a date range with pagination
    Page<DeliveryHistory> findByDeliveryDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // Find delayed deliveries (delay > 0) with pagination
    @Query("SELECT dh FROM DeliveryHistory dh WHERE dh.delayMinutes > 0")
    Page<DeliveryHistory> findDelayedDeliveries(Pageable pageable);
    
    // Find on-time deliveries (delay <= 0) with pagination
    @Query("SELECT dh FROM DeliveryHistory dh WHERE dh.delayMinutes <= 0")
    Page<DeliveryHistory> findOnTimeDeliveries(Pageable pageable);
    
    // Statistics methods
    @Query("SELECT COUNT(dh) FROM DeliveryHistory dh WHERE dh.customer.id = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(dh) FROM DeliveryHistory dh WHERE dh.customer.id = :customerId AND dh.delayMinutes <= :maxDelay")
    long countByCustomerIdAndDelayMinutesLessThanEqual(
        @Param("customerId") Long customerId, 
        @Param("maxDelay") int maxDelay
    );
    
    @Query("SELECT AVG(dh.delayMinutes) FROM DeliveryHistory dh WHERE dh.customer.id = :customerId")
    Optional<Double> findAverageDelayByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(dh) FROM DeliveryHistory dh WHERE dh.tour.id = :tourId")
    long countByTourId(@Param("tourId") Long tourId);
    
    @Query("SELECT COUNT(dh) FROM DeliveryHistory dh WHERE dh.tour.id = :tourId AND dh.delayMinutes <= :maxDelay")
    long countByTourIdAndDelayMinutesLessThanEqual(
        @Param("tourId") Long tourId, 
        @Param("maxDelay") int maxDelay
    );
    
    @Query("SELECT AVG(dh.delayMinutes) FROM DeliveryHistory dh WHERE dh.tour.id = :tourId")
    Optional<Double> findAverageDelayByTourId(@Param("tourId") Long tourId);
    
    @Query("SELECT COALESCE(SUM(dh.delayMinutes), 0) FROM DeliveryHistory dh WHERE dh.tour.id = :tourId")
    Optional<Long> findTotalDelayByTourId(@Param("tourId") Long tourId);
    
    // Existing methods for backward compatibility
    List<DeliveryHistory> findByCustomerId(Long customerId);
    List<DeliveryHistory> findByTourId(Long tourId);
    List<DeliveryHistory> findByDeliveryDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Calculate average delay by day of week
    @Query("""
        SELECT dh.dayOfWeek, AVG(dh.delayMinutes) 
        FROM DeliveryHistory dh 
        WHERE dh.delayMinutes > 0 
        GROUP BY dh.dayOfWeek
    """)
    List<Object[]> findAverageDelayByDayOfWeek();
    
    // Find deliveries by planned time window
    @Query("""
        SELECT dh FROM DeliveryHistory dh 
        WHERE dh.plannedTime BETWEEN :startTime AND :endTime
        ORDER BY dh.plannedTime
    """)
    List<DeliveryHistory> findDeliveriesByTimeWindow(
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
    
    // Find delivery history with customer and delivery details
    @Query("""
        SELECT dh FROM DeliveryHistory dh 
        JOIN FETCH dh.customer c 
        JOIN FETCH dh.delivery d 
        WHERE dh.id = :id
    """)
    Optional<DeliveryHistory> findByIdWithDetails(@Param("id") Long id);
}
