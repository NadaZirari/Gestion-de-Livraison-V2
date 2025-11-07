package com.delivrey.service.impl;

import com.delivrey.entity.Delivery;
import com.delivrey.service.TourOptimizer;
import java.util.*;

public class ClarkeWrightOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList<>();
        }

        // Si moins de 3 points, l'algorithme n'est pas nécessaire
        if (deliveries.size() <= 2) {
            return new ArrayList<>(deliveries);
        }

        // Étape 1: Créer des routes individuelles pour chaque point
        List<List<Delivery>> routes = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            List<Delivery> route = new ArrayList<>();
            route.add(delivery);
            routes.add(route);
        }

        // Étape 2: Calculer les économies pour chaque paire de points
        List<Savings> savingsList = calculateSavings(deliveries);
        
        // Trier les économies par ordre décroissant
        savingsList.sort(Comparator.comparingDouble(Savings::getSaving).reversed());

        // Étape 3: Fusionner les routes en fonction des économies
        for (Savings savings : savingsList) {
            int route1Index = findRouteContaining(routes, savings.getI());
            int route2Index = findRouteContaining(routes, savings.getJ());

            // Vérifier si les points sont dans des routes différentes
            if (route1Index != route2Index) {
                List<Delivery> route1 = routes.get(route1Index);
                List<Delivery> route2 = routes.get(route2Index);

                // Vérifier si on peut fusionner les routes
                if (canMerge(route1, route2)) {
                    mergeRoutes(route1, route2);
                    routes.remove(route2Index);
                }
            }
        }

        // Retourner la première route (toutes les routes ont été fusionnées en une seule)
        return routes.get(0);
    }

    private List<Savings> calculateSavings(List<Delivery> deliveries) {
        List<Savings> savingsList = new ArrayList<>();
        
        // Pour chaque paire de points, calculer l'économie
        for (int i = 0; i < deliveries.size(); i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                Delivery di = deliveries.get(i);
                Delivery dj = deliveries.get(j);
                
                // Calculer l'économie (distance entre le dépôt et i + distance entre le dépôt et j - distance entre i et j)
                double saving = calculateDistance(0, 0, di.getLatitude(), di.getLongitude()) +
                              calculateDistance(0, 0, dj.getLatitude(), dj.getLongitude()) -
                              calculateDistance(di.getLatitude(), di.getLongitude(), 
                                              dj.getLatitude(), dj.getLongitude());
                
                savingsList.add(new Savings(i, j, saving));
            }
        }
        
        return savingsList;
    }

    private int findRouteContaining(List<List<Delivery>> routes, int deliveryIndex) {
        for (int i = 0; i < routes.size(); i++) {
            for (Delivery d : routes.get(i)) {
                if (d.getId() == deliveryIndex) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean canMerge(List<Delivery> route1, List<Delivery> route2) {
        // Ici, vous pourriez ajouter des contraintes de capacité, etc.
        return true;
    }

    private void mergeRoutes(List<Delivery> route1, List<Delivery> route2) {
        // Fusionner les deux routes en une seule
        route1.addAll(route2);
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

    // Classe interne pour représenter les économies
    private static class Savings {
        private final int i;
        private final int j;
        private final double saving;

        public Savings(int i, int j, double saving) {
            this.i = i;
            this.j = j;
            this.saving = saving;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        public double getSaving() {
            return saving;
        }
    }
}
