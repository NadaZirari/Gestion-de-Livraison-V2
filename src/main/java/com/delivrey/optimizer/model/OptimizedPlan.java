package com.delivrey.optimizer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Représente un plan d'optimisation pour une tournée de livraison.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizedPlan {
    
    /**
     * Liste des livraisons ordonnées selon l'optimisation.
     */
    private List<DeliveryOrder> orderedDeliveries;
    
    /**
     * Recommandations générées par l'optimiseur.
     */
    private List<String> recommendations;
    
    /**
     * Prédictions d'itinéraires.
     */
    private List<RoutePrediction> predictedRoutes;
    
    /**
     * Métadonnées sur l'optimisation.
     */
    private OptimizationMetadata metadata;
    
    /**
     * Représente une commande de livraison dans le plan optimisé.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryOrder {
        private Long deliveryId;
        private int sequenceNumber;
        private LocalDateTime estimatedArrivalTime;
        private LocalDateTime estimatedDepartureTime;
        private double distanceFromPrevious; // en kilomètres
        private Duration travelTimeFromPrevious;
        private Map<String, Object> additionalInfo;
    }
    
    /**
     * Prédiction d'itinéraire entre deux points.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutePrediction {
        private Long fromDeliveryId;
        private Long toDeliveryId;
        private double distance; // en kilomètres
        private Duration estimatedDuration;
        private Duration estimatedDurationWithTraffic;
        private List<Map<String, Object>> steps; // Étapes détaillées de l'itinéraire
    }
    
    /**
     * Métadonnées sur l'optimisation.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptimizationMetadata {
        private String optimizerName;
        private String optimizerVersion;
        private LocalDateTime optimizationTime;
        private Duration optimizationDuration;
        private int totalDeliveries;
        private double totalDistance; // en kilomètres
        private Duration totalDuration;
        private Map<String, Object> statistics;
        private Map<String, Object> constraintsUsed;
    }
}
