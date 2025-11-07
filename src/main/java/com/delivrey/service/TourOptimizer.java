package com.delivrey.service;

import com.delivrey.entity.Delivery;
import java.util.List;

public interface TourOptimizer {
    List<Delivery> calculateOptimalTour(List<Delivery> deliveries);
}
