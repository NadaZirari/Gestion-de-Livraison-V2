package com.delivrey.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class AIOptimizationResponse {
    private List<OptimizedDelivery> optimizedDeliveries;
    private List<String> recommendations;
    private String routeSummary;
    
    @Data
    public static class OptimizedDelivery {
        private Long deliveryId;
        private Integer sequence;
        private String estimatedArrivalTime;
        private String timeWindow;
    }
}
