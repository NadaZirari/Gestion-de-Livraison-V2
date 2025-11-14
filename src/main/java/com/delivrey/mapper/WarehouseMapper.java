package com.delivrey.mapper;

import com.delivrey.dto.WarehouseDTO;
import com.delivrey.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WarehouseMapper {
    
    WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "openingHours", source = "openingHours")
    WarehouseDTO toDto(Warehouse warehouse);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "openingHours", source = "openingHours", defaultValue = "06:00-22:00")
    Warehouse toEntity(WarehouseDTO dto);
    
    default void updateFromDto(WarehouseDTO dto, @MappingTarget Warehouse entity) {
        if (dto == null) {
            return;
        }
        
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        
        if (dto.getLatitude() != 0) {
            entity.setLatitude(dto.getLatitude());
        }
        
        if (dto.getLongitude() != 0) {
            entity.setLongitude(dto.getLongitude());
        }
        
        if (dto.getOpeningHours() != null) {
            entity.setOpeningHours(dto.getOpeningHours());
        }
    }
}
