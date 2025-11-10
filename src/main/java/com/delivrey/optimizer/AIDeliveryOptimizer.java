package com.delivrey.optimizer;

import com.delivrey.entity.Delivery;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AI-based delivery optimizer that uses distance-based optimization for delivery routes.
 * This is a simplified version that sorts deliveries by location.
 */
@Component
public class AIDeliveryOptimizer implements DeliveryOptimizer {

    @Override
    public List<Delivery> optimize(List<Delivery> deliveries) {
        // Simple optimization: sort by customer location (latitude + longitude)
        // This is a placeholder - in a real implementation, this would use more sophisticated logic
        return deliveries.stream()
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(delivery -> 
                Math.abs(delivery.getLatitude()) + Math.abs(delivery.getLongitude())))
            .collect(Collectors.toList());
    }
    
    @Override
    public String getOptimizerType() {
        return "DISTANCE_BASED_OPTIMIZER";
    }
}
