package com.delivrey.repository;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryRepositoryTest {

    @Test
    void testFindByStatus() {
        DeliveryRepository deliveryRepo = mock(DeliveryRepository.class);

        Delivery d1 = new Delivery();
        d1.setId(1L);
        d1.setStatus(DeliveryStatus.PENDING);

        Delivery d2 = new Delivery();
        d2.setId(2L);
        d2.setStatus(DeliveryStatus.DELIVERED);

        List<Delivery> pendingDeliveries = Arrays.asList(d1);

        when(deliveryRepo.findByStatus(DeliveryStatus.PENDING)).thenReturn(pendingDeliveries);

        List<Delivery> result = deliveryRepo.findByStatus(DeliveryStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DeliveryStatus.PENDING, result.get(0).getStatus());
    }
}
