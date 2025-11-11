package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryHistory;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.exception.NotFoundException;
import com.delivrey.repository.DeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryHistoryService {
    
    private final DeliveryHistoryRepository deliveryHistoryRepository;
    
    @Transactional
    public void createHistoryFromTour(Tour tour) {
        if (tour.getTourStatus() == TourStatus.COMPLETED && tour.getDeliveries() != null) {
            tour.getDeliveries().forEach(delivery -> {
                DeliveryHistory history = new DeliveryHistory();
                history.setCustomer(delivery.getCustomer());
                history.setDelivery(delivery);
                history.setTour(tour);
                history.setDeliveryDate(tour.getTourDate());
                history.setPlannedTime(extractPlannedTime(delivery));
                history.setActualTime(LocalTime.now());
                history.calculateDelay();
                
                deliveryHistoryRepository.save(history);
            });
        }
    }

    @Transactional(readOnly = true)
    public Page<DeliveryHistory> findAll(Pageable pageable) {
        log.debug("Fetching all delivery histories with pagination: {}", pageable);
        return deliveryHistoryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<DeliveryHistory> findById(Long id) {
        log.debug("Fetching delivery history with id: {}", id);
        return deliveryHistoryRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Page<DeliveryHistory> findByCustomerId(Long customerId, Pageable pageable) {
        log.debug("Fetching delivery histories for customer id: {}", customerId);
        return deliveryHistoryRepository.findByCustomerId(customerId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<DeliveryHistory> findByTourId(Long tourId, Pageable pageable) {
        log.debug("Fetching delivery histories for tour id: {}", tourId);
        return deliveryHistoryRepository.findByTourId(tourId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<DeliveryHistory> findByDeliveryDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Fetching delivery histories between {} and {}", startDate, endDate);
        return deliveryHistoryRepository.findByDeliveryDateBetween(startDate, endDate, pageable);
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getCustomerDeliveryStats(Long customerId) {
        log.debug("Fetching delivery stats for customer id: {}", customerId);
        return Map.of(
            "totalDeliveries", deliveryHistoryRepository.countByCustomerId(customerId),
            "onTimeDeliveries", deliveryHistoryRepository.countByCustomerIdAndDelayMinutesLessThanEqual(customerId, 0),
            "averageDelay", deliveryHistoryRepository.findAverageDelayByCustomerId(customerId).orElse(0.0)
        );
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getTourDeliveryStats(Long tourId) {
        log.debug("Fetching delivery stats for tour id: {}", tourId);
        return Map.of(
            "totalDeliveries", deliveryHistoryRepository.countByTourId(tourId),
            "onTimeDeliveries", deliveryHistoryRepository.countByTourIdAndDelayMinutesLessThanEqual(tourId, 0),
            "averageDelay", deliveryHistoryRepository.findAverageDelayByTourId(tourId).orElse(0.0),
            "totalDelay", deliveryHistoryRepository.findTotalDelayByTourId(tourId).orElse(0L)
        );
    }
    
    private LocalTime extractPlannedTime(Delivery delivery) {
        try {
            if (delivery.getCustomer() != null && delivery.getCustomer().getPreferredTimeSlot() != null) {
                String[] timeParts = delivery.getCustomer().getPreferredTimeSlot().split("-");
                if (timeParts.length > 0) {
                    return LocalTime.parse(timeParts[0].trim());
                }
            }
        } catch (Exception e) {
            log.warn("Erreur lors de l'extraction de l'heure planifiée pour la livraison: {}", delivery.getId(), e);
        }
        return LocalTime.NOON; // Heure par défaut si non spécifiée
    }
}
