

package com.delivrey.controller;

import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.repository.TourRepository;
import com.delivrey.service.DeliveryHistoryService;
import com.delivrey.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TourControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourService tourService;

    @MockBean
    private DeliveryHistoryService deliveryHistoryService;

    @Test
    public void completeTour_ShouldReturnUpdatedTour() throws Exception {
        // Given
        Long tourId = 1L;
        Tour tour = new Tour();
        tour.setId(tourId);
        tour.setTourStatus(TourStatus.PLANNED);
        
        when(tourService.updateTourStatus(tourId, TourStatus.COMPLETED)).thenAnswer(invocation -> {
            tour.setTourStatus(TourStatus.COMPLETED);
            return tour;
        });

        // When & Then
        mockMvc.perform(put("/api/tours/{id}/complete", tourId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tourId))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(tourService, times(1)).updateTourStatus(tourId, TourStatus.COMPLETED);
    }
}
