package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import com.delivrey.entity.Vehicle;
import com.delivrey.entity.Warehouse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DeliveryMapper.class})
public interface TourMapper {
    
    TourMapper INSTANCE = Mappers.getMapper(TourMapper.class);
    
    @Mapping(target = "date", source = "tourDate")
    @Mapping(target = "status", source = "tourStatus")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "driverId", source = "vehicle.driver.id")
    @Mapping(target = "deliveryIds", expression = "java(mapDeliveriesToIds(tour.getDeliveries()))")
    TourDTO toDto(Tour tour);
    
    @Mapping(target = "tourDate", source = "date")
    @Mapping(target = "tourStatus", source = "status")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapToVehicle")
    @Mapping(target = "warehouse", source = "warehouseId", qualifiedByName = "mapToWarehouse")
    @Mapping(target = "deliveries", ignore = true) // Géré séparément dans le service
    Tour toEntity(TourDTO tourDTO);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tourDate", source = "date")
    @Mapping(target = "tourStatus", source = "status")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapToVehicle")
    @Mapping(target = "warehouse", source = "warehouseId", qualifiedByName = "mapToWarehouse")
    @Mapping(target = "deliveries", ignore = true) // Géré séparément dans le service
    void updateTourFromDto(TourDTO tourDTO, @MappingTarget Tour tour);
    
    default List<Long> mapDeliveriesToIds(List<com.delivrey.entity.Delivery> deliveries) {
        if (deliveries == null) {
            return null;
        }
        return deliveries.stream()
                .map(com.delivrey.entity.Delivery::getId)
                .collect(Collectors.toList());
    }
    
    @Named("mapToVehicle")
    default Vehicle mapToVehicle(Long vehicleId) {
        if (vehicleId == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        return vehicle;
    }
    
    @Named("mapToWarehouse")
    default Warehouse mapToWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseId);
        return warehouse;
    }
}
