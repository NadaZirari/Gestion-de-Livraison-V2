package com.delivrey.mapper;

import com.delivrey.dto.VehicleDTO;
import com.delivrey.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "registrationNumber", source = "registrationNumber")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "maxWeight", source = "maxWeight")
    @Mapping(target = "maxVolume", source = "maxVolume")
    @Mapping(target = "maxDeliveries", source = "maxDeliveries")
    @NonNull
    VehicleDTO toDto(@NonNull Vehicle vehicle);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "registrationNumber", source = "registrationNumber")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "maxWeight", source = "maxWeight")
    @Mapping(target = "maxVolume", source = "maxVolume")
    @Mapping(target = "maxDeliveries", source = "maxDeliveries")
    @NonNull
    Vehicle toEntity(@NonNull VehicleDTO dto);
    
    default Vehicle fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        return vehicle;
    }
}
