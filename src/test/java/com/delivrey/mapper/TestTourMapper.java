package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class TestTourMapper implements TourMapper {
    
    @Override
    public TourDTO toDto(Tour tour) {
        if (tour == null) {
            return null;
        }
        
        TourDTO dto = new TourDTO();
        dto.setId(tour.getId());
        // Copy other fields as needed for testing
        return dto;
    }
    
    @Override
    public Tour toEntity(TourDTO tourDTO) {
        if (tourDTO == null) {
            return null;
        }
        
        Tour tour = new Tour();
        tour.setId(tourDTO.getId());
        // Copy other fields as needed for testing
        return tour;
    }
    
    @Override
    public TourDTO toDtoWithDeliveries(Tour tour) {
        if (tour == null) {
            return null;
        }
        
        TourDTO dto = toDto(tour);
        if (tour.getDeliveries() != null) {
            dto.setDeliveryIds(tour.getDeliveries().stream()
                    .map(delivery -> delivery.getId())
                    .toList());
        } else {
            dto.setDeliveryIds(Collections.emptyList());
        }
        
        return dto;
    }
}
