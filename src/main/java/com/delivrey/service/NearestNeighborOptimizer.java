package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.util.GeoUtils;
import java.util.ArrayList;
import java.util.List;

public class NearestNeighborOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        List<Delivery> remaining = new ArrayList<>(deliveries);
        List<Delivery> ordered = new ArrayList<>();
        if (remaining.isEmpty()) return ordered;

        // start from first as depot is handled externally (if needed)
        Delivery current = remaining.remove(0);
        ordered.add(current);

        while (!remaining.isEmpty()) {
            Delivery next = null;
            double minDist = Double.MAX_VALUE;
            for (Delivery d : remaining) {
                double dist = GeoUtils.haversineKm(current.getLatitude(), current.getLongitude(),
                                                   d.getLatitude(), d.getLongitude());
                if (dist < minDist) {
                    minDist = dist;
                    next = d;
                }
            }
            if (next == null) break;
            ordered.add(next);
            remaining.remove(next);
            current = next;
        }
        return ordered;
    }
}
