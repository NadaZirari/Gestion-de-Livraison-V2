package com.delivrey.repository;

import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("SELECT DISTINCT t FROM Tour t LEFT JOIN FETCH t.deliveries")
    List<Tour> findAllWithDeliveries();
    
    @Query("SELECT DISTINCT t FROM Tour t LEFT JOIN FETCH t.deliveries LEFT JOIN FETCH t.warehouse WHERE t.id = :id")
    Optional<Tour> findByIdWithDeliveriesAndWarehouse(@Param("id") Long id);
    
    @Query("SELECT t FROM Tour t LEFT JOIN FETCH t.deliveries WHERE t.id = :id")
    Optional<Tour> findByIdWithDeliveries(@Param("id") Long id);
    
    List<Tour> findByVehicleId(Long vehicleId);
    
    List<Tour> findByTourStatus(TourStatus status);
    
    Page<Tour> findByTourStatus(TourStatus status, Pageable pageable);
    
    List<Tour> findByWarehouseId(Long warehouseId);
    
    List<Tour> findByTourDateBetween(LocalDate startDate, LocalDate endDate);
}
