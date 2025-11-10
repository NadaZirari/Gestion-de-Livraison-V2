package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TourMapper {
    
    TourMapper INSTANCE = Mappers.getMapper(TourMapper.class);
    
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
