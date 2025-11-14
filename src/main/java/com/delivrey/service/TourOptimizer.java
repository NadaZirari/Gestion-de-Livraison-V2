package com.delivrey.service;

import com.delivrey.dto.DeliveryDTO;
import java.util.List;

/**
 * Service pour l'optimisation des tournées de livraison.
 * Calcule l'itinéraire optimal pour une liste de livraisons données.
 */
public interface TourOptimizer {
    
    /**
     * Calcule l'itinéraire optimal pour une liste de livraisons.
     *
     * @param deliveries Liste des livraisons à optimiser
     * @return Liste des livraisons réorganisées dans l'ordre optimal
     */
    List<DeliveryDTO> calculateOptimalTour(List<DeliveryDTO> deliveries);
}
