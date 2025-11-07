package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.util.GeoUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ClarkeWrightOptimizer implements TourOptimizer
 */
public class ClarkeWrightOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.size() <= 1) return deliveries == null ? Collections.emptyList() : new ArrayList<>(deliveries);


        Delivery warehouse = deliveries.get(0);
        List<Delivery> clients = new ArrayList<>(deliveries.subList(1, deliveries.size()));

        Map<Delivery, LinkedList<Delivery>> routes = new HashMap<>();
        for (Delivery c : clients) {
            LinkedList<Delivery> route = new LinkedList<>();
            route.add(c);
            routes.put(c, route);
        }

        class Saving { Delivery i, j; double value; }
        List<Saving> savings = new ArrayList<>();
        for (int a = 0; a < clients.size(); a++) {
            for (int b = a+1; b < clients.size(); b++) {
                Delivery i = clients.get(a);
                Delivery j = clients.get(b);
                double wi = GeoUtils.haversineKm(warehouse.getLatitude(), warehouse.getLongitude(), i.getLatitude(), i.getLongitude());
                double wj = GeoUtils.haversineKm(warehouse.getLatitude(), warehouse.getLongitude(), j.getLatitude(), j.getLongitude());
                double ij = GeoUtils.haversineKm(i.getLatitude(), i.getLongitude(), j.getLatitude(), j.getLongitude());
                Saving s = new Saving();
                s.i = i; s.j = j; s.value = wi + wj - ij;
                savings.add(s);
            }
        }


        savings.sort(Comparator.comparingDouble((Saving s) -> s.value).reversed());

        for (Saving s : savings) {
            LinkedList<Delivery> routeI = findRouteContaining(routes, s.i);
            LinkedList<Delivery> routeJ = findRouteContaining(routes, s.j);
            if (routeI == null || routeJ == null || routeI == routeJ) continue;

            Delivery iFront = routeI.getFirst();
            Delivery iBack = routeI.getLast();
            Delivery jFront = routeJ.getFirst();
            Delivery jBack = routeJ.getLast();

            boolean canMerge = false;

            if (iBack.equals(s.i) && jFront.equals(s.j)) {
                routeI.addAll(routeJ);
                canMerge = true;
            } else if (jBack.equals(s.j) && iFront.equals(s.i)) {

            	routeJ.addAll(routeI);

            	routes.values().remove(routeI);
                routes.put(routeJ.getFirst(), routeJ);
                canMerge = true;
            } else if (iBack.equals(s.i) && jBack.equals(s.j)) {

            	Collections.reverse(routeJ);
                routeI.addAll(routeJ);
                canMerge = true;
            } else if (iFront.equals(s.i) && jFront.equals(s.j)) {
                Collections.reverse(routeI);
                routeI.addAll(routeJ);
                canMerge = true;
            }

            if (canMerge) {
            
            	
                Map<Delivery, LinkedList<Delivery>> newRoutes = new HashMap<>();
                for (LinkedList<Delivery> r : new HashSet<>(routes.values())) {
                    if (r.size() == 0) continue;
                    newRoutes.put(r.getFirst(), r);
                }
                routes = newRoutes;
            }
        }

        List<Delivery> result = new ArrayList<>();
        
        LinkedList<Delivery> merged = new LinkedList<>();
        
        for (LinkedList<Delivery> r : routes.values()) {
            merged.addAll(r);
        }
        result.add(warehouse); 
        result.addAll(merged);
        return result;
    }

    private LinkedList<Delivery> findRouteContaining(Map<Delivery, LinkedList<Delivery>> routes, Delivery d) {
        for (LinkedList<Delivery> r : routes.values()) {
            if (r.contains(d)) return r;
        }
        return null;
    }
}
