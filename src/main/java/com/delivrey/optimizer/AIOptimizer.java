package com.delivrey.optimizer;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;

import java.util.List;

public interface AIOptimizer {
    Tour optimizeTour(Tour tour, List<Delivery> deliveries);
}
