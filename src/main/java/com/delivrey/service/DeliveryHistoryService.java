package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryHistory;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.repository.DeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

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
    
    private LocalTime extractPlannedTime(Delivery delivery) {
        try {
            if (delivery.getCustomer() != null && delivery.getCustomer().getPreferredTimeSlot() != null) {
                String[] timeParts = delivery.getCustomer().getPreferredTimeSlot().split("-");
                if (timeParts.length > 0) {
                    return LocalTime.parse(timeParts[0].trim());
                }
            }
        } catch (Exception e) {
            // Fall through to default
        }
        return LocalTime.NOON; // Default time if not specified
    }
}
