package com.delivrey.service;

import com.delivrey.dto.DeliveryHistoryDto;
import com.delivrey.mapper.DeliveryHistoryMapper;
import com.delivrey.service.impl.DeliveryHistoryServiceImpl;
import com.delivrey.entity.DeliveryHistory;
import com.delivrey.entity.DeliveryStatus;
import com.delivrey.entity.TourStatus;
import com.delivrey.repository.DeliveryHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryHistoryServiceTest {

    @Mock
    private DeliveryHistoryRepository deliveryHistoryRepository;
    
    @Mock
    private DeliveryHistoryMapper deliveryHistoryMapper;

    @InjectMocks
    private DeliveryHistoryServiceImpl deliveryHistoryService;

    private DeliveryHistory testHistory;
    private DeliveryHistoryDto testHistoryDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testHistory = new DeliveryHistory();
        testHistory.setId(1L);
        testHistory.setDeliveryDate(LocalDate.now());
        
        testHistoryDto = DeliveryHistoryDto.builder()
            .id(1L)
            .deliveryDate(LocalDate.now())
            .deliveryStatus(DeliveryStatus.DELIVERED)
            .tourStatus(TourStatus.COMPLETED)
            .build();
        
        pageable = PageRequest.of(0, 10, Sort.by("deliveryDate").descending());
    }

    @Test
    void whenFindById_thenReturnHistory() {
        // Arrange
        when(deliveryHistoryRepository.findById(1L)).thenReturn(Optional.of(testHistory));
        when(deliveryHistoryMapper.toDto(testHistory)).thenReturn(testHistoryDto);

        // Act
        Optional<DeliveryHistoryDto> found = deliveryHistoryService.findById(1L);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        verify(deliveryHistoryRepository, times(1)).findById(1L);
        verify(deliveryHistoryMapper, times(1)).toDto(testHistory);
    }

    @Test
    void whenFindAll_thenReturnPageOfHistories() {
        // Arrange
        Page<DeliveryHistory> page = new PageImpl<>(Collections.singletonList(testHistory));
        when(deliveryHistoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(deliveryHistoryMapper.toDto(testHistory)).thenReturn(testHistoryDto);

        // Act
        Page<DeliveryHistoryDto> result = deliveryHistoryService.findAll(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(deliveryHistoryRepository, times(1)).findAll(pageable);
        verify(deliveryHistoryMapper, times(1)).toDto(testHistory);
    }
    
    @Test
    void whenFindByCustomerId_thenReturnPageOfHistories() {
        // Arrange
        Page<DeliveryHistory> page = new PageImpl<>(Collections.singletonList(testHistory));
        when(deliveryHistoryRepository.findByCustomerId(anyLong(), any(Pageable.class))).thenReturn(page);
        when(deliveryHistoryMapper.toDto(testHistory)).thenReturn(testHistoryDto);
        
        // Act
        Page<DeliveryHistoryDto> result = deliveryHistoryService.findByCustomerId(1L, pageable);
        
        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(deliveryHistoryRepository, times(1)).findByCustomerId(1L, pageable);
        verify(deliveryHistoryMapper, times(1)).toDto(testHistory);
    }
    
    @Test
    void whenFindByTourId_thenReturnPageOfHistories() {
        // Arrange
        Page<DeliveryHistory> page = new PageImpl<>(Collections.singletonList(testHistory));
        when(deliveryHistoryRepository.findByTourId(anyLong(), any(Pageable.class))).thenReturn(page);
        when(deliveryHistoryMapper.toDto(testHistory)).thenReturn(testHistoryDto);
        
        // Act
        Page<DeliveryHistoryDto> result = deliveryHistoryService.findByTourId(1L, pageable);
        
        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(deliveryHistoryRepository, times(1)).findByTourId(1L, pageable);
        verify(deliveryHistoryMapper, times(1)).toDto(testHistory);
    }
}
