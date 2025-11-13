package com.delivrey.mapper;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.entity.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "volume", source = "volume")
    @Mapping(target = "timeWindow", source = "timeWindow")
    @Mapping(target = "status", source = "status")
    @NonNull
    DeliveryDTO toDto(@NonNull Delivery delivery);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "volume", source = "volume")
    @Mapping(target = "timeWindow", source = "timeWindow")
    @Mapping(target = "status", source = "status")
    @NonNull
    Delivery toEntity(@NonNull DeliveryDTO dto);
    
    /**
     * Met à jour une entité Delivery à partir d'un DTO
     * @param dto Le DTO source
     * @param delivery L'entité à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateDeliveryFromDto(DeliveryDTO dto, @MappingTarget Delivery delivery);
    
    default Delivery fromId(Long id) {
        if (id == null) {
            return null;
        }
        Delivery delivery = new Delivery();
        delivery.setId(id);
        return delivery;
    }
}
