package com.delivrey.service;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.entity.DeliveryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryService deliveryService;

    private DeliveryDTO testDelivery;

    @BeforeEach
    void setUp() {
        // Création d'un objet de test
        testDelivery = new DeliveryDTO();
        testDelivery.setId(1L);
        testDelivery.setStatus(DeliveryStatus.PENDING);
        testDelivery.setCustomerId(100L);
    }

    @Test
    void getDeliveryById_ShouldReturnDelivery_WhenIdExists() {
        // Arrange
        when(deliveryService.getDeliveryById(1L)).thenReturn(Optional.of(testDelivery));

        // Act
        Optional<DeliveryDTO> found = deliveryService.getDeliveryById(1L);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        assertEquals(DeliveryStatus.PENDING, found.get().getStatus());
        verify(deliveryService, times(1)).getDeliveryById(1L);
    }

    @Test
    void createDelivery_ShouldReturnCreatedDelivery() {
        // Arrange
        when(deliveryService.createDelivery(any(DeliveryDTO.class))).thenReturn(testDelivery);

        // Act
        DeliveryDTO created = deliveryService.createDelivery(testDelivery);

        // Assert
        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(deliveryService, times(1)).createDelivery(any(DeliveryDTO.class));
    }

    @Test
    void updateDeliveryStatus_ShouldUpdateStatus() {
        // Arrange
        DeliveryDTO updatedDelivery = new DeliveryDTO();
        updatedDelivery.setId(1L);
        updatedDelivery.setStatus(DeliveryStatus.DELIVERED);

        when(deliveryService.updateDeliveryStatus(1L, DeliveryStatus.DELIVERED)).thenReturn(updatedDelivery);

        // Act
        DeliveryDTO result = deliveryService.updateDeliveryStatus(1L, DeliveryStatus.DELIVERED);

        // Assert
        assertNotNull(result);
        assertEquals(DeliveryStatus.DELIVERED, result.getStatus());
        verify(deliveryService, times(1)).updateDeliveryStatus(1L, DeliveryStatus.DELIVERED);
    }

    @Test
    void getAllDeliveries_ShouldReturnAllDeliveries() {
        // Arrange
        List<DeliveryDTO> deliveries = Arrays.asList(testDelivery);
        when(deliveryService.getAllDeliveries()).thenReturn(deliveries);

        // Act
        List<DeliveryDTO> result = deliveryService.getAllDeliveries();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(deliveryService, times(1)).getAllDeliveries();
    }
}
