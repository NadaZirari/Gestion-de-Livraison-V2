package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import com.delivrey.entity.Vehicle;
import com.delivrey.entity.Warehouse;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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
            // Note: La propriété getDriver() n'existe pas sur Vehicle
            // Cette partie doit être gérée différemment si nécessaire
        }
        
        if (tour.getWarehouse() != null) {
            dto.setWarehouseId(tour.getWarehouse().getId());
        }
        
        if (tour.getDeliveries() != null && !tour.getDeliveries().isEmpty()) {
            dto.setDeliveryIds(mapDeliveriesToIds(tour.getDeliveries()));
        }
        
        dto.setAlgorithmUsed(tour.getAlgorithmUsed());
        
        return dto;
    }
    
    @Override
    public List<Long> mapDeliveriesToIds(List<com.delivrey.entity.Delivery> deliveries) {
        if (deliveries == null) {
            return null;
        }
        return deliveries.stream()
                .filter(Objects::nonNull)
                .map(com.delivrey.entity.Delivery::getId)
                .collect(Collectors.toList());
    }
    
    @Override
    public Tour toEntity(TourDTO tourDTO) {
        if (tourDTO == null) {
            return null;
        }
        
        Tour tour = new Tour();
        updateTourFromDto(tourDTO, tour);
        return tour;
    }
    
    @Override
    public void updateTourFromDto(TourDTO tourDTO, @MappingTarget Tour tour) {
        if (tourDTO == null || tour == null) {
            return;
        }
        
        tour.setId(tourDTO.getId());
        tour.setTourDate(tourDTO.getDate());
        
        if (tourDTO.getStatus() != null) {
            tour.setTourStatus(tourDTO.getStatus());
        }
        
        // Vehicle et Warehouse sont gérés séparément dans le service
        // car ils nécessitent des appels à la base de données
        
        tour.setAlgorithmUsed(tourDTO.getAlgorithmUsed());
    }
    
    @Named("mapToVehicle")
    @Override
    public Vehicle mapToVehicle(Long vehicleId) {
        if (vehicleId == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        return vehicle;
    }
    
    @Named("mapToWarehouse")
    @Override
    public Warehouse mapToWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseId);
        return warehouse;
    }
}