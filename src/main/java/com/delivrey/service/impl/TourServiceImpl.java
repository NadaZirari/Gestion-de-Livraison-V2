package com.delivrey.service.impl;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.event.TourStatusChangeEvent;
import com.delivrey.exception.EntityNotFoundException;
import com.delivrey.mapper.TourMapper;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.repository.TourRepository;
import com.delivrey.service.TourService;
import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.event.TourStatusChangeEvent;
import com.delivrey.exception.EntityNotFoundException;
import com.delivrey.mapper.TourMapper;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.repository.TourRepository;
import com.delivrey.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    
    @Qualifier("nearestNeighbor")
    private final TourOptimizer nearestNeighborOptimizer;
    
    @Qualifier("clarkeWright")
    private final TourOptimizer clarkeWrightOptimizer;
    
    @Qualifier("aiOptimizer")
    private final TourOptimizer aiOptimizer;
    
    private final ApplicationEventPublisher eventPublisher;
    private final TourMapper tourMapper;

    @Override
    public Tour getTourById(Long id) {
        return tourRepository.findByIdWithDeliveriesAndWarehouse(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));
    }

    @Override
    public List<Tour> getAllTours() {
        return tourRepository.findAllWithDeliveries();
    }

    @Override
    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Override
    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public TourDTO updateTourStatus(Long id, TourStatus newStatus) {
        return tourRepository.findByIdWithDeliveries(id)
                .map(tour -> {
                    TourStatus oldStatus = tour.getTourStatus();
                    if (oldStatus != newStatus) {
                        tour.setTourStatus(newStatus);
                        Tour updatedTour = tourRepository.save(tour);
                        
                        // Publish the status change event
                        eventPublisher.publishEvent(new TourStatusChangeEvent(this, updatedTour, oldStatus, newStatus));
                        return tourMapper.toDto(updatedTour);
                    }
                    return tourMapper.toDto(tour);
                })
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + id));
    }

    @Override
    public Optional<Tour> findById(Long id) {
        return tourRepository.findByIdWithDeliveries(id);
    }

    @Override
    public List<Delivery> getOptimizedTour(Long tourId, String algorithm) {
        Tour tour = tourRepository.findByIdWithDeliveriesAndWarehouse(tourId) 
            .orElseThrow(() -> new RuntimeException("Tour not found with id: " + tourId));

        List<Delivery> deliveries = new ArrayList<>(tour.getDeliveries());
        
        // Add warehouse as the starting point if it exists
        if (tour.getWarehouse() != null) {
            Delivery warehouseDelivery = new Delivery();
            warehouseDelivery.setId(-1L); // Temporary ID for warehouse
            warehouseDelivery.setLatitude(tour.getWarehouse().getLatitude());
            warehouseDelivery.setLongitude(tour.getWarehouse().getLongitude());
            warehouseDelivery.setAddress("Warehouse");
            deliveries.add(0, warehouseDelivery);
        }
        
        // Choose optimization algorithm
        TourOptimizer optimizer = switch (algorithm.toUpperCase()) {
            case "CLARKE_WRIGHT" -> clarkeWrightOptimizer;
            case "AI" -> aiOptimizer;
            default -> nearestNeighborOptimizer; // Default to nearest neighbor
        };

        // Optimize the tour
        return optimizer.calculateOptimalTour(deliveries);
    }

    @Override
    public double getTotalDistance(Long tourId, String algorithm) {
        List<Delivery> optimizedRoute = getOptimizedTour(tourId, algorithm);
        if (optimizedRoute.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        Delivery previous = optimizedRoute.get(0);
        
        for (int i = 1; i < optimizedRoute.size(); i++) {
            Delivery current = optimizedRoute.get(i);
            totalDistance += calculateDistance(
                previous.getLatitude(), previous.getLongitude(),
                current.getLatitude(), current.getLongitude()
            );
            previous = current;
        }

        return totalDistance;
    }

    // Utility method to calculate distance between two geographical points
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance in kilometers
    }
}
