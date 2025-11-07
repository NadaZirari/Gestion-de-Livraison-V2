package com.delivrey.repository;

import com.delivrey.entity.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
    
    // Find all delivery histories for a specific customer
    List<DeliveryHistory> findByCustomerId(Long customerId);
    
    // Find all delivery histories for a specific tour
    List<DeliveryHistory> findByTourId(Long tourId);
    
    // Find delivery histories within a date range
    List<DeliveryHistory> findByDeliveryDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find delayed deliveries (delay > 0)
    @Query("SELECT dh FROM DeliveryHistory dh WHERE dh.delayMinutes > 0")
    List<DeliveryHistory> findDelayedDeliveries();
    
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
