package com.delivrey.optimizer;

import com.delivrey.entity.Delivery;
import java.util.List;

/**
 * Interface for delivery optimization strategies.
 */
public interface DeliveryOptimizer {
    
    /**
     * Optimizes the delivery order based on certain criteria.
     * @param deliveries List of deliveries to optimize
     * @return Optimized list of deliveries
     */
    List<Delivery> optimize(List<Delivery> deliveries);
    
    /**
     * Gets the type/name of the optimizer.
     * @return The optimizer type/name
     */
    String getOptimizerType();
}
