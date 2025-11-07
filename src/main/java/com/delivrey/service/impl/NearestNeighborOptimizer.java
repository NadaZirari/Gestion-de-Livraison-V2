package com.delivrey.service.impl;

import com.delivrey.entity.Delivery;
import com.delivrey.service.TourOptimizer;
import java.util.*;

public class NearestNeighborOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList<>();
        }

        List<Delivery> optimizedRoute = new ArrayList<>();
        List<Delivery> remainingDeliveries = new ArrayList<>(deliveries);
        
        // Démarrer avec le premier point de livraison
        Delivery current = remainingDeliveries.remove(0);
        optimizedRoute.add(current);

        // Tant qu'il reste des livraisons à traiter
        while (!remainingDeliveries.isEmpty()) {
            Delivery nearest = findNearest(current, remainingDeliveries);
            optimizedRoute.add(nearest);
            remainingDeliveries.remove(nearest);
            current = nearest;
        }

        return optimizedRoute;
    }

    private Delivery findNearest(Delivery current, List<Delivery> deliveries) {
        Delivery nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Delivery delivery : deliveries) {
            double distance = calculateDistance(
                current.getLatitude(), current.getLongitude(),
                delivery.getLatitude(), delivery.getLongitude()
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                nearest = delivery;
            }
        }
        
        return nearest;
    }

    // Méthode utilitaire pour calculer la distance entre deux points géographiques (formule de Haversine)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Rayon de la Terre en kilomètres
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance en kilomètres
    }
}
