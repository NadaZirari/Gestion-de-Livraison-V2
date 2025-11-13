package com.delivrey.service;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.TourStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing tours.
 */
public interface TourService {
    
    // Basic CRUD operations
    TourDTO getTourById(Long id);
    
    List<TourDTO> getAllTours();
    
    Page<TourDTO> getAllTours(Pageable pageable);
    
    TourDTO createTour(TourDTO tourDTO);
    
    TourDTO updateTour(Long id, TourDTO tourDTO);
    
    void deleteTour(Long id);
    
    // Status management
    TourDTO updateTourStatus(Long id, TourStatus status);
    
    // Additional methods
    List<Delivery> getOptimizedTour(Long tourId, String algorithm);
    
    double getTotalDistance(Long tourId, String algorithm);
    
    // Find methods
    Optional<TourDTO> findById(Long id);
    
    List<TourDTO> findByStatus(TourStatus status);
    
    Page<TourDTO> findByStatus(TourStatus status, Pageable pageable);
    
    List<TourDTO> findByVehicleId(Long vehicleId);
    
    List<TourDTO> findByWarehouseId(Long warehouseId);
    
    List<TourDTO> findByDateRange(String startDate, String endDate);
}
