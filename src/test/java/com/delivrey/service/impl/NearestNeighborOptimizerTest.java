package com.delivrey.service.impl;

import com.delivrey.entity.Delivery;
import com.delivrey.service.NearestNeighborOptimizer;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NearestNeighborOptimizerTest {

    @Test
    void testCalculateOptimalTour() {
        NearestNeighborOptimizer optimizer = new NearestNeighborOptimizer();

        Delivery depot = new Delivery();
        depot.setId(-1L);
        depot.setLatitude(0.0);
        depot.setLongitude(0.0);

        Delivery d1 = new Delivery();
        d1.setId(1L);
        d1.setLatitude(1.0);
        d1.setLongitude(1.0);

        Delivery d2 = new Delivery();
        d2.setId(2L);
        d2.setLatitude(2.0);
        d2.setLongitude(2.0);

        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(depot);
        deliveries.add(d1);
        deliveries.add(d2);

        List<Delivery> result = optimizer.calculateOptimalTour(deliveries);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(depot.getId(), result.get(0).getId());
    }
}
