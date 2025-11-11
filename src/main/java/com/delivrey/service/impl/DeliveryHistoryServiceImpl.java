package com.delivrey.service.impl;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryHistory;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.repository.DeliveryHistoryRepository;
import com.delivrey.service.DeliveryHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryHistoryServiceImpl implements DeliveryHistoryService {

    private final DeliveryHistoryRepository deliveryHistoryRepository;

    @Override
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

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<DeliveryHistory> findAll(@NonNull Pageable pageable) {
        log.debug("Fetching all delivery histories with pagination: {}", pageable);
        return deliveryHistoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<DeliveryHistory> findById(@NonNull Long id) {
        log.debug("Fetching delivery history with id: {}", id);
        return deliveryHistoryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<DeliveryHistory> findByCustomerId(@NonNull Long customerId, @NonNull Pageable pageable) {
        log.debug("Fetching delivery histories for customer id: {}", customerId);
        return deliveryHistoryRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<DeliveryHistory> findByTourId(@NonNull Long tourId, @NonNull Pageable pageable) {
        log.debug("Fetching delivery histories for tour id: {}", tourId);
        return deliveryHistoryRepository.findByTourId(tourId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<DeliveryHistory> findByDeliveryDateBetween(
            @NonNull LocalDate startDate,
            @NonNull LocalDate endDate,
            @NonNull Pageable pageable) {
        log.debug("Fetching delivery histories between {} and {}", startDate, endDate);
        return deliveryHistoryRepository.findByDeliveryDateBetween(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Map<String, Object> getCustomerDeliveryStats(@NonNull Long customerId) {
        log.debug("Fetching delivery stats for customer id: {}", customerId);
        Long totalDeliveries = deliveryHistoryRepository.countByCustomerId(customerId);
        Long onTimeDeliveries = deliveryHistoryRepository.countByCustomerIdAndDelayMinutesLessThanEqual(customerId, 0);
        Double averageDelay = deliveryHistoryRepository.findAverageDelayByCustomerId(customerId).orElse(0.0);

        Objects.requireNonNull(totalDeliveries, "Total deliveries cannot be null");
        Objects.requireNonNull(onTimeDeliveries, "On-time deliveries cannot be null");
        Objects.requireNonNull(averageDelay, "Average delay cannot be null");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDeliveries", totalDeliveries);
        stats.put("onTimeDeliveries", onTimeDeliveries);
        stats.put("averageDelay", averageDelay);

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Map<String, Object> getTourDeliveryStats(@NonNull Long tourId) {
        log.debug("Fetching delivery stats for tour id: {}", tourId);
        Long totalDeliveries = deliveryHistoryRepository.countByTourId(tourId);
        Long onTimeDeliveries = deliveryHistoryRepository.countByTourIdAndDelayMinutesLessThanEqual(tourId, 0);
        Double averageDelay = deliveryHistoryRepository.findAverageDelayByTourId(tourId).orElse(0.0);
        Long totalDelay = deliveryHistoryRepository.findTotalDelayByTourId(tourId).orElse(0L);

        Objects.requireNonNull(totalDeliveries, "Total deliveries cannot be null");
        Objects.requireNonNull(onTimeDeliveries, "On-time deliveries cannot be null");
        Objects.requireNonNull(averageDelay, "Average delay cannot be null");
        Objects.requireNonNull(totalDelay, "Total delay cannot be null");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDeliveries", totalDeliveries);
        stats.put("onTimeDeliveries", onTimeDeliveries);
        stats.put("averageDelay", averageDelay);
        stats.put("totalDelay", totalDelay);

        return stats;
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
