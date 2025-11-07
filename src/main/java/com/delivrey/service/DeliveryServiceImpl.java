package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;
import com.delivrey.exception.ResourceNotFoundException;
import com.delivrey.repository.DeliveryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the DeliveryService interface.
 */
@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Delivery> getDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }

    @Override
    public Delivery createDelivery(Delivery delivery) {
        if (delivery.getStatus() == null) {
            delivery.setStatus(DeliveryStatus.PENDING);
        }
        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery updateDelivery(Long id, Delivery deliveryDetails) {
        return deliveryRepository.findById(id)
            .map(existingDelivery -> {
                if (deliveryDetails.getAddress() != null) {
                    existingDelivery.setAddress(deliveryDetails.getAddress());
                }
                if (deliveryDetails.getLatitude() != null) {
                    existingDelivery.setLatitude(deliveryDetails.getLatitude());
                }
                if (deliveryDetails.getLongitude() != null) {
                    existingDelivery.setLongitude(deliveryDetails.getLongitude());
                }
                if (deliveryDetails.getWeight() != null) {
                    existingDelivery.setWeight(deliveryDetails.getWeight());
                }
                if (deliveryDetails.getVolume() != null) {
                    existingDelivery.setVolume(deliveryDetails.getVolume());
                }
                if (deliveryDetails.getTimeWindow() != null) {
                    existingDelivery.setTimeWindow(deliveryDetails.getTimeWindow());
                }
                if (deliveryDetails.getStatus() != null) {
                    existingDelivery.setStatus(deliveryDetails.getStatus());
                }
                if (deliveryDetails.getCustomer() != null) {
                    existingDelivery.setCustomer(deliveryDetails.getCustomer());
                }
                return deliveryRepository.save(existingDelivery);
            })
            .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
    }

    @Override
    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
        deliveryRepository.delete(delivery);
    }

    @Override
    public Delivery updateDeliveryStatus(Long id, DeliveryStatus status) {
        return deliveryRepository.findById(id)
            .map(delivery -> {
                delivery.setStatus(status);
                return deliveryRepository.save(delivery);
            })
            .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Delivery> getDeliveriesByStatus(DeliveryStatus status, Pageable pageable) {
        return deliveryRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByCustomerId(Long customerId) {
        return deliveryRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findNearbyDeliveries(double latitude, double longitude, double radiusKm, DeliveryStatus status) {
        return deliveryRepository.findNearbyDeliveries(latitude, longitude, radiusKm, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByTimeWindow(String timeWindow) {
        return deliveryRepository.findByTimeWindow(timeWindow);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByWeightRange(Double minWeight, Double maxWeight) {
        return deliveryRepository.findByWeightBetween(minWeight, maxWeight);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByVolumeRange(Double minVolume, Double maxVolume) {
        return deliveryRepository.findByVolumeBetween(minVolume, maxVolume);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(DeliveryStatus status) {
        return deliveryRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Delivery> getDeliveryWithCustomer(Long id) {
        return deliveryRepository.findByIdWithCustomer(id);
    }
}
