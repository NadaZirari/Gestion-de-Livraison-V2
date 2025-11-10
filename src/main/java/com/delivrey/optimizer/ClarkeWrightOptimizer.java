package com.delivrey.optimizer;

import com.delivrey.entity.Delivery;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ClarkeWrightOptimizer implements TourOptimizer {
    
    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.size() <= 1) {
            return deliveries;
        }

        // If there are only 2 deliveries, return them as is
        if (deliveries.size() == 2) {
            return new ArrayList<>(deliveries);
        }

        // For simplicity, we'll implement a simplified version of the Clarke-Wright algorithm
        // that builds routes by merging the most efficient pairs of deliveries
        
        // Start with individual routes for each delivery
        List<List<Delivery>> routes = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            List<Delivery> route = new ArrayList<>();
            route.add(delivery);
            routes.add(route);
        }

        // Calculate savings for all pairs of deliveries
        List<Savings> savings = calculateSavings(deliveries);
        
        // Sort savings in descending order
        savings.sort(Comparator.comparingDouble(Savings::getSaving).reversed());
        
        // Merge routes based on savings
        for (Savings saving : savings) {
            int route1Idx = findRouteContaining(routes, saving.getI());
            int route2Idx = findRouteContaining(routes, saving.getJ());
            
            // If the deliveries are in different routes and neither is in the middle of a route
            if (route1Idx != route2Idx && route1Idx != -1 && route2Idx != -1) {
                List<Delivery> route1 = routes.get(route1Idx);
                List<Delivery> route2 = routes.get(route2Idx);
                
                // If the first route ends with i and the second starts with j, merge them
                if (route1.get(route1.size() - 1).equals(saving.getI()) && 
                    route2.get(0).equals(saving.getJ())) {
                    route1.addAll(route2);
                    routes.remove(route2Idx);
                }
                // If the first route starts with i and the second ends with j, merge them in reverse
                else if (route1.get(0).equals(saving.getI()) && 
                         route2.get(route2.size() - 1).equals(saving.getJ())) {
                    Collections.reverse(route2);
                    route2.addAll(route1);
                    routes.set(route1Idx, route2);
                    routes.remove(route2Idx);
                }
            }
        }
        
        // Return the first route (in a real implementation, we might have multiple routes)
        return routes.isEmpty() ? new ArrayList<>() : routes.get(0);
    }
    
    private List<Savings> calculateSavings(List<Delivery> deliveries) {
        List<Savings> savings = new ArrayList<>();
        
        for (int i = 0; i < deliveries.size(); i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                Delivery di = deliveries.get(i);
                Delivery dj = deliveries.get(j);
                
                // Calculate savings using the distance from the depot to each delivery
                double saving = calculateDistance(0, 0, di.getLatitude(), di.getLongitude()) +
                              calculateDistance(0, 0, dj.getLatitude(), dj.getLongitude()) -
                              calculateDistance(di.getLatitude(), di.getLongitude(), 
                                              dj.getLatitude(), dj.getLongitude());
                
                savings.add(new Savings(di, dj, saving));
            }
        }
        
        return savings;
    }
    
    private int findRouteContaining(List<List<Delivery>> routes, Delivery delivery) {
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).contains(delivery)) {
                return i;
            }
        }
        return -1;
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
    
    private static class Savings {
        private final Delivery i;
        private final Delivery j;
        private final double saving;
        
        public Savings(Delivery i, Delivery j, double saving) {
            this.i = i;
            this.j = j;
            this.saving = saving;
        }
        
        public Delivery getI() {
            return i;
        }
        
        public Delivery getJ() {
            return j;
        }
        
        public double getSaving() {
            return saving;
        }
    }
}
