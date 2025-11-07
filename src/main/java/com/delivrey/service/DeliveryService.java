package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing deliveries.
 */
public interface DeliveryService {
    
    // Basic CRUD operations
    List<Delivery> getAllDeliveries();
    
    Optional<Delivery> getDeliveryById(Long id);
    
    Delivery createDelivery(Delivery delivery);
    
    Delivery updateDelivery(Long id, Delivery deliveryDetails);
    
    void deleteDelivery(Long id);
    
    // Status management
    Delivery updateDeliveryStatus(Long id, DeliveryStatus status);
    
    List<Delivery> getDeliveriesByStatus(DeliveryStatus status);
    
    Page<Delivery> getDeliveriesByStatus(DeliveryStatus status, Pageable pageable);
    
    // Customer-related queries
    List<Delivery> getDeliveriesByCustomerId(Long customerId);
    
    // Location-based queries
    List<Delivery> findNearbyDeliveries(double latitude, double longitude, double radiusKm, DeliveryStatus status);
    
    // Time-based queries
    List<Delivery> getDeliveriesByTimeWindow(String timeWindow);
    
    List<Delivery> getDeliveriesCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Dimension-based queries
    List<Delivery> getDeliveriesByWeightRange(Double minWeight, Double maxWeight);
    
    List<Delivery> getDeliveriesByVolumeRange(Double minVolume, Double maxVolume);
    
    // Statistics
    long countByStatus(DeliveryStatus status);
    
    // Eager loading
    Optional<Delivery> getDeliveryWithCustomer(Long id);
}
