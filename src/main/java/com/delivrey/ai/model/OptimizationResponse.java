package com.delivrey.ai.model;

import lombok.Data;
import java.util.List;

@Data
public class OptimizationResponse {
    private List<OptimizedDelivery> optimizedDeliveries;
    private RouteSummary routeSummary;
    private List<String> recommendations;
    private List<String> warnings;
    
    @Data
    public static class OptimizedDelivery {
        private Long deliveryId;
        private int position;
        private Long assignedVehicleId;
        private String estimatedArrivalTime;
        private String estimatedDepartureTime;
        private double distanceFromPrevious;
    }
    
    @Data
    public static class RouteSummary {
        private double totalDistance;
        private String estimatedTotalTime;
        private int numberOfVehiclesUsed;
        private double utilizationRate;
    }
}
