package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TourMapperImpl implements TourMapper {
    
    @Override
    public TourDTO toDto(Tour tour) {
        if (tour == null) {
            return null;
        }
        
        TourDTO dto = new TourDTO();
        dto.setId(tour.getId());
        dto.setDate(tour.getTourDate());
        dto.setStatus(tour.getTourStatus());
        
        if (tour.getVehicle() != null) {
            dto.setVehicleId(tour.getVehicle().getId());
        }
        
        return dto;
    }
    
    @Override
    public Tour toEntity(TourDTO tourDTO) {
        if (tourDTO == null) {
            return null;
        }
        
        Tour tour = new Tour();
        tour.setId(tourDTO.getId());
        tour.setTourDate(tourDTO.getDate());
        
        if (tourDTO.getStatus() != null) {
            tour.setTourStatus(tourDTO.getStatus());
        }
        
        // Note: The vehicle would need to be set by the service layer
        
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
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}
