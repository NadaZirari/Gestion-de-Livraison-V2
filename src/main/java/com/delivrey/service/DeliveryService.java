package com.delivrey.service;

import com.delivrey.dto.DeliveryDTO;
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
    List<DeliveryDTO> getAllDeliveries();
    
    Optional<DeliveryDTO> getDeliveryById(Long id);
    
    DeliveryDTO createDelivery(DeliveryDTO deliveryDTO);
    
    DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO);
    
    void deleteDelivery(Long id);
    
    // Status management
    DeliveryDTO updateDeliveryStatus(Long id, DeliveryStatus status);
    
    List<DeliveryDTO> getDeliveriesByStatus(DeliveryStatus status);
    
    Page<DeliveryDTO> getDeliveriesByStatus(DeliveryStatus status, Pageable pageable);
    
    // Customer-related queries
    List<DeliveryDTO> getDeliveriesByCustomerId(Long customerId);
    
    // Location-based queries
    List<DeliveryDTO> findNearbyDeliveries(double latitude, double longitude, double radiusKm, DeliveryStatus status);
    
    // Time-based queries
    List<DeliveryDTO> getDeliveriesByTimeWindow(String timeWindow);
    
    List<DeliveryDTO> getDeliveriesCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Dimension-based queries
    List<DeliveryDTO> getDeliveriesByWeightRange(Double minWeight, Double maxWeight);
    
    List<DeliveryDTO> getDeliveriesByVolumeRange(Double minVolume, Double maxVolume);
    
    // Statistics
    long countByStatus(DeliveryStatus status);
}
