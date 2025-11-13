package com.delivrey.service;

import com.delivrey.entity.*;
import com.delivrey.repository.DeliveryHistoryRepository;
import com.delivrey.service.impl.DeliveryHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryHistoryServiceTest {

    @Mock
    private DeliveryHistoryRepository deliveryHistoryRepository;

    @InjectMocks
    private DeliveryHistoryServiceImpl deliveryHistoryService;

    private Tour testTour;
    private Delivery testDelivery1;
    private Delivery testDelivery2;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Créer un client de test
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test Customer");
        testCustomer.setAddress("123 Test St");

        // Créer un tour de test
        testTour = new Tour();
        testTour.setId(1L);
        testTour.setTourStatus(TourStatus.PLANNED);
        testTour.setTourDate(LocalDate.now());

        // Créer des livraisons de test
        testDelivery1 = new Delivery();
        testDelivery1.setId(1L);
        testDelivery1.setCustomer(testCustomer);
        testDelivery1.setTour(testTour);

        testDelivery2 = new Delivery();
        testDelivery2.setId(2L);
        testDelivery2.setCustomer(testCustomer);
        testDelivery2.setTour(testTour);

        // Associer les livraisons au tour
        testTour.setDeliveries(Arrays.asList(testDelivery1, testDelivery2));
    }

    @Test
    public void whenTourStatusIsCompleted_thenCreateHistoryForEachDelivery() {
        // Given
        testTour.setTourStatus(TourStatus.COMPLETED);
        testCustomer.setPreferredTimeSlot("09:00-12:00");

        // When
        deliveryHistoryService.createHistoryFromTour(testTour);

        // Then - Vérifier que save a été appelé 2 fois (une fois par livraison)
        verify(deliveryHistoryRepository, times(2)).save(any(DeliveryHistory.class));
        
        // Vérifier les détails de l'historique créé
        ArgumentCaptor<DeliveryHistory> captor = ArgumentCaptor.forClass(DeliveryHistory.class);
        verify(deliveryHistoryRepository, times(2)).save(captor.capture());
        
        // Vérifier les propriétés de l'historique
        DeliveryHistory savedHistory = captor.getAllValues().get(0);
        assertNotNull(savedHistory);
        assertEquals(testCustomer, savedHistory.getCustomer());
        assertNotNull(savedHistory.getPlannedTime());
        assertNotNull(savedHistory.getActualTime());
        assertNotNull(savedHistory.getDeliveryDate());
    }

    @Test
    public void whenTourStatusIsNotCompleted_thenNoHistoryCreated() {
        // Given
        testTour.setTourStatus(TourStatus.IN_PROGRESS);

        // When
        deliveryHistoryService.createHistoryFromTour(testTour);

        // Then - Vérifier que save n'a jamais été appelé
        verify(deliveryHistoryRepository, never()).save(any(DeliveryHistory.class));
    }

    @Test
    public void whenTourHasNoDeliveries_thenNoHistoryCreated() {
        // Given
        testTour.setTourStatus(TourStatus.COMPLETED);
        testTour.setDeliveries(Collections.emptyList());

        // When
        deliveryHistoryService.createHistoryFromTour(testTour);

        // Then
        verify(deliveryHistoryRepository, never()).save(any(DeliveryHistory.class));
    }
    
    @Test
    public void whenGetCustomerDeliveryStats_thenReturnStats() {
        // Given
        when(deliveryHistoryRepository.countByCustomerId(1L)).thenReturn(10L);
        when(deliveryHistoryRepository.countByCustomerIdAndDelayMinutesLessThanEqual(1L, 0)).thenReturn(8L);
        when(deliveryHistoryRepository.findAverageDelayByCustomerId(1L)).thenReturn(Optional.of(5.5));

        // When
        Map<String, Object> stats = deliveryHistoryService.getCustomerDeliveryStats(1L);

        // Then
        assertNotNull(stats);
        assertEquals(10L, stats.get("totalDeliveries"));
        assertEquals(8L, stats.get("onTimeDeliveries"));
        assertEquals("80.00%", stats.get("onTimePercentage"));
        assertEquals("5.50 minutes", stats.get("averageDelay"));
    }
    
    @Test
    public void whenGetTourDeliveryStats_thenReturnStats() {
        // Given
        when(deliveryHistoryRepository.countByTourId(1L)).thenReturn(5L);
        when(deliveryHistoryRepository.countByTourIdAndDelayMinutesLessThanEqual(1L, 0)).thenReturn(4L);
        when(deliveryHistoryRepository.findAverageDelayByTourId(1L)).thenReturn(Optional.of(3.5));
        when(deliveryHistoryRepository.findTotalDelayByTourId(1L)).thenReturn(Optional.of(15L));

        // When
        Map<String, Object> stats = deliveryHistoryService.getTourDeliveryStats(1L);

        // Then
        assertNotNull(stats);
        assertEquals(5L, stats.get("totalDeliveries"));
        assertEquals(4L, stats.get("onTimeDeliveries"));
        assertEquals("80.00%", stats.get("onTimePercentage"));
        assertEquals("3.50 minutes", stats.get("averageDelay"));
        assertEquals("15 minutes", stats.get("totalDelay"));
    }
    
    @Test
    public void whenFindAll_thenReturnPageOfHistories() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(deliveryHistoryRepository.findAll(pageable)).thenReturn(Page.empty());
        
        // When
        Page<DeliveryHistory> result = deliveryHistoryService.findAll(pageable);
        
        // Then
        assertNotNull(result);
        verify(deliveryHistoryRepository).findAll(pageable);
    }
    
    @Test
    public void whenFindByCustomerId_thenReturnPageOfHistories() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(deliveryHistoryRepository.findByCustomerId(1L, pageable)).thenReturn(Page.empty());
        
        // When
        Page<DeliveryHistory> result = deliveryHistoryService.findByCustomerId(1L, pageable);
        
        // Then
        assertNotNull(result);
        verify(deliveryHistoryRepository).findByCustomerId(1L, pageable);
    }
}
