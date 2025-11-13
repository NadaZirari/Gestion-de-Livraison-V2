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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourServiceImpl implements TourService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final ApplicationEventPublisher eventPublisher;
    
    @Qualifier("nearestNeighbor")
    private final TourOptimizer nearestNeighborOptimizer;

    @Qualifier("clarkeWright")
    private final TourOptimizer clarkeWrightOptimizer;

    @Qualifier("aiOptimizer")
    private final TourOptimizer aiOptimizer;

    @Override
    @Transactional(readOnly = true)
    public TourDTO getTourById(Long id) {
        return tourRepository.findByIdWithDeliveriesAndWarehouse(id)
                .map(tourMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourDTO> getAllTours() {
        return tourRepository.findAllWithDeliveries().stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TourDTO> getAllTours(Pageable pageable) {
        return tourRepository.findAll(pageable)
                .map(tourMapper::toDto);
    }

    @Override
    @Transactional
    public TourDTO createTour(TourDTO tourDTO) {
        Tour tour = tourMapper.toEntity(tourDTO);
        Tour savedTour = tourRepository.save(tour);
        return tourMapper.toDto(savedTour);
    }

    @Override
    @Transactional
    public TourDTO updateTour(Long id, TourDTO tourDTO) {
        return tourRepository.findById(id)
                .map(existingTour -> {
                    tourMapper.updateTourFromDto(tourDTO, existingTour);
                    Tour updatedTour = tourRepository.save(existingTour);
                    return tourMapper.toDto(updatedTour);
                })
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new EntityNotFoundException("Tour not found with id: " + id);
        }
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
    @Transactional(readOnly = true)
    public Optional<TourDTO> findById(Long id) {
        return tourRepository.findByIdWithDeliveries(id)
                .map(tourMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourDTO> findByStatus(TourStatus status) {
        return tourRepository.findByTourStatus(status).stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TourDTO> findByStatus(TourStatus status, Pageable pageable) {
        return tourRepository.findByTourStatus(status, pageable)
                .map(tourMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourDTO> findByVehicleId(Long vehicleId) {
        return tourRepository.findByVehicleId(vehicleId).stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourDTO> findByWarehouseId(Long warehouseId) {
        return tourRepository.findByWarehouseId(warehouseId).stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourDTO> findByDateRange(String startDateStr, String endDateStr) {
        try {
            LocalDate startDate = LocalDate.parse(startDateStr, DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);

            return tourRepository.findByTourDateBetween(startDate, endDate).stream()
                    .map(tourMapper::toDto)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use ISO format (yyyy-MM-dd)", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getOptimizedTour(Long tourId, String algorithm) {
        Tour tour = tourRepository.findByIdWithDeliveriesAndWarehouse(tourId)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + tourId));

        List<Delivery> deliveries = tour.getDeliveries();

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
    @Transactional(readOnly = true)
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
