package com.delivrey.mapper;

import com.delivrey.dto.DeliveryHistoryDto;
import com.delivrey.entity.DeliveryHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DeliveryHistoryMapper {

    DeliveryHistoryMapper INSTANCE = Mappers.getMapper(DeliveryHistoryMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "delivery.id", target = "deliveryId")
    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "delivery.status", target = "deliveryStatus")
    @Mapping(source = "tour.tourStatus", target = "tourStatus")
    DeliveryHistoryDto toDto(DeliveryHistory deliveryHistory);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    @Mapping(target = "tour", ignore = true)
    DeliveryHistory toEntity(DeliveryHistoryDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    @Mapping(target = "tour", ignore = true)
    void updateDeliveryHistoryFromDto(DeliveryHistoryDto dto, @org.mapstruct.MappingTarget DeliveryHistory entity);
}
