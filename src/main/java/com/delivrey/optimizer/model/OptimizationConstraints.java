package com.delivrey.optimizer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Set;

/**
 * Contraintes pour l'optimisation des tournées de livraison.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationConstraints {
    /**
     * Durée maximale totale de la tournée.
     */
    private Duration maxTotalDuration;
    
    /**
     * Nombre maximal de livraisons par véhicule.
     */
    private Integer maxDeliveriesPerVehicle;
    
    /**
     * Poids maximal total des livraisons.
     */
    private Double maxTotalWeight;
    
    /**
     * Volume maximal total des livraisons.
     */
    private Double maxTotalVolume;
    
    /**
     * Indique si les conditions de trafic doivent être prises en compte.
     */
    @Builder.Default
    private boolean considerTraffic = true;
    
    /**
     * Plages horaires à éviter (en minutes depuis minuit).
     * Par exemple: [[480, 540]] pour éviter 8h-9h.
     */
    private Set<TimeWindow> avoidTimeWindows;
    
    /**
     * Identifiants des livraisons à inclure obligatoirement.
     */
    private Set<Long> includeDeliveryIds;
    
    /**
     * Identifiants des livraisons à exclure.
     */
    private Set<Long> excludeDeliveryIds;
    
    /**
     * Représente une plage horaire.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeWindow {
        private int startMinute;
        private int endMinute;
    }
}
