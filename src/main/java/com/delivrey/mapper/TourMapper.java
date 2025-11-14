package com.delivrey.mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Tour;
import com.delivrey.entity.Vehicle;
import com.delivrey.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {DeliveryMapper.class},
    implementationName = "TourMapperCustomImpl"
)
public interface TourMapper {

    TourMapper INSTANCE = Mappers.getMapper(TourMapper.class);

    @Mapping(target = "date", source = "tourDate")
    @Mapping(target = "status", source = "tourStatus")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "deliveryIds", expression = "java(mapDeliveriesToIds(tour.getDeliveries()))")
    TourDTO toDto(Tour tour);

    @Mapping(target = "tourDate", source = "date")
    @Mapping(target = "tourStatus", source = "status")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapToVehicle")
    @Mapping(target = "warehouse", source = "warehouseId", qualifiedByName = "mapToWarehouse")
    @Mapping(target = "deliveries", ignore = true)
    Tour toEntity(TourDTO dto);

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
    
    /**
     * Met à jour une entité Tour existante avec les valeurs d'un DTO
     * @param dto Le DTO contenant les nouvelles valeurs
     * @param tour L'entité à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tourDate", source = "date")
    @Mapping(target = "tourStatus", source = "status")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapToVehicle")
    @Mapping(target = "warehouse", source = "warehouseId", qualifiedByName = "mapToWarehouse")
    @Mapping(target = "deliveries", ignore = true)
    void updateTourFromDto(TourDTO dto, @MappingTarget Tour tour);
}
