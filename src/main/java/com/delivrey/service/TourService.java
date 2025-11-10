package com.delivrey.service;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;

import java.util.List;
import java.util.Optional;

public interface TourService {
    // CRUD Operations
    Tour getTourById(Long id);
    List<Tour> getAllTours();
    Tour saveTour(Tour tour);
    void deleteTour(Long id);
    
    // Additional methods
    Optional<Tour> findById(Long id);
    List<Delivery> getOptimizedTour(Long tourId, String algorithm);
    double getTotalDistance(Long tourId, String algorithm);
    
    // Tour status management
    TourDTO updateTourStatus(Long id, TourStatus status);
}
