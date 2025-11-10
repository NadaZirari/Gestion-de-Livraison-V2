package com.delivrey.optimizer;

import com.delivrey.entity.Delivery;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NearestNeighborOptimizer implements TourOptimizer {
    
    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty() || deliveries.size() == 1) {
            return deliveries;
        }

        List<Delivery> optimizedRoute = new ArrayList<>();
        Set<Delivery> unvisited = new HashSet<>(deliveries);
        
        // Start with the first delivery
        Delivery current = deliveries.get(0);
        optimizedRoute.add(current);
        unvisited.remove(current);

        // Find nearest neighbor until all deliveries are visited
        while (!unvisited.isEmpty()) {
            Delivery nearest = findNearestNeighbor(current, unvisited);
            optimizedRoute.add(nearest);
            unvisited.remove(nearest);
            current = nearest;
        }

        return optimizedRoute;
    }

    private Delivery findNearestNeighbor(Delivery current, Set<Delivery> candidates) {
        Delivery nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Delivery candidate : candidates) {
            double distance = calculateDistance(
                current.getLatitude(), 
                current.getLongitude(),
                candidate.getLatitude(),
                candidate.getLongitude()
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                nearest = candidate;
            }
        }

        return nearest;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
