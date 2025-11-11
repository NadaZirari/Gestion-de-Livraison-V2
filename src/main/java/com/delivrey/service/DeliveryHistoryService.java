package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryHistory;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.exception.NotFoundException;
import com.delivrey.repository.DeliveryHistoryRepository;
import com.delivrey.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

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
    
/**
     * Récupère l'historique des livraisons pour un tour spécifique
     * @param tourId L'identifiant du tour
     * @return La liste des historiques de livraison pour le tour
     * @throws NotFoundException Si le tour n'est pas trouvé
     */
    @Transactional(readOnly = true)
    public List<DeliveryHistory> findByTourId(Long tourId) {
        log.debug("Recherche de l'historique des livraisons pour le tour ID: {}", tourId);
        List<DeliveryHistory> history = deliveryHistoryRepository.findByTourId(tourId);
        
        if (history.isEmpty()) {
            log.warn("Aucun historique trouvé pour le tour ID: {}", tourId);
            throw new NotFoundException("Aucun historique trouvé pour le tour ID: " + tourId);
        }
        
        return history;
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
