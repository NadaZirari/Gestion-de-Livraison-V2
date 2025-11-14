package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

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
    public void updateTourFromDto(TourDTO dto, @MappingTarget Tour tour) {
        if (dto == null || tour == null) {
            return;
        }
        
        if (dto.getDate() != null) {
            tour.setTourDate(dto.getDate());
        }
        if (dto.getStatus() != null) {
            tour.setTourStatus(dto.getStatus());
        }
        // Note: Les livraisons sont ignorées comme spécifié dans le mapper principal
    }
}
