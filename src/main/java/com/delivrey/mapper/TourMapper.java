package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import org.springframework.stereotype.Component;

@Component
public interface TourMapper {
    
    TourDTO toDto(Tour tour);
    
    Tour toEntity(TourDTO tourDTO);
    
    default TourDTO toDtoWithDeliveries(Tour tour) {
        if (tour == null) {
            return null;
        }
        
        TourDTO dto = toDto(tour);
        if (tour.getDeliveries() != null) {
            dto.setDeliveryIds(tour.getDeliveries().stream()
                    .map(delivery -> delivery.getId())
                    .toList());
        }
        
        return dto;
    }
}
