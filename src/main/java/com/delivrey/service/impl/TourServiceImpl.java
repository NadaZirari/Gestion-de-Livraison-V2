package com.delivrey.service.impl;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import java.util.ArrayList;
import com.delivrey.repository.DeliveryRepository;
import com.delivrey.repository.TourRepository;
import com.delivrey.service.TourOptimizer;
import com.delivrey.service.TourService;
import java.util.List;

public class TourServiceImpl implements TourService {

    private final DeliveryRepository deliveryRepository;
    private final TourRepository tourRepository;
    private final TourOptimizer nearestNeighborOptimizer;
    private final TourOptimizer clarkeWrightOptimizer;

    public TourServiceImpl(DeliveryRepository deliveryRepository,
                         TourRepository tourRepository, 
                         TourOptimizer nearestNeighborOptimizer, 
                         TourOptimizer clarkeWrightOptimizer) {
        this.deliveryRepository = deliveryRepository;
        this.tourRepository = tourRepository;
        this.nearestNeighborOptimizer = nearestNeighborOptimizer;
        this.clarkeWrightOptimizer = clarkeWrightOptimizer;
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
        TourOptimizer optimizer = "CLARKE_WRIGHT".equalsIgnoreCase(algorithm) 
            ? clarkeWrightOptimizer 
            : nearestNeighborOptimizer;

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

    @Override
    public Tour getTourById(Long id) {
        return tourRepository.findByIdWithDeliveries(id)
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

    // Méthode utilitaire pour calculer la distance entre deux points géographiques
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Rayon de la Terre en kilomètres
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance en kilomètres
    }
}
