package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import com.delivrey.entity.Vehicle;
import com.delivrey.entity.Warehouse;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TestTourMapper implements TourMapper {
    
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
        if (tour.getWarehouse() != null) {
            dto.setWarehouseId(tour.getWarehouse().getId());
        }
        // Pour les tests, on peut ignorer les IDs de livraison ou les initialiser si nécessaire
        return dto;
    }
    
    @Override
    public Tour toEntity(TourDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Tour tour = new Tour();
        updateTourFromDto(dto, tour);
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
        // Les autres champs sont gérés par MapStruct
    }
    
    @Named("mapToVehicle")
    public Vehicle mapToVehicle(Long vehicleId) {
        if (vehicleId == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        return vehicle;
    }
    
    @Named("mapToWarehouse")
    public Warehouse mapToWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseId);
        return warehouse;
    }
    
    @Override
    public List<Long> mapDeliveriesToIds(List<com.delivrey.entity.Delivery> deliveries) {
        if (deliveries == null) {
            return Collections.emptyList();
        }
        return deliveries.stream()
                .map(com.delivrey.entity.Delivery::getId)
                .toList();
    }
}
