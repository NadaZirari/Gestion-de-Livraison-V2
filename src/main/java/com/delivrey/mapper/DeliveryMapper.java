package com.delivrey.mapper;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.entity.Customer;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface DeliveryMapper {
    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);
    
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", expression = "java(getCustomerName(delivery.getCustomer()))")
    @Mapping(target = "tourId", source = "tour.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @NonNull
    DeliveryDTO toDto(@NonNull Delivery delivery);
    
    @Mapping(target = "customer", source = "customerId", qualifiedByName = "customerFromId")
    @Mapping(target = "tour", source = "tourId", qualifiedByName = "tourFromId")
    @Mapping(target = "deliveryHistories", ignore = true)
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
    @Mapping(target = "deliveryHistories", ignore = true)
    @Mapping(target = "customer", source = "customerId", qualifiedByName = "customerFromId")
    @Mapping(target = "tour", source = "tourId", qualifiedByName = "tourFromId")
    void updateDeliveryFromDto(DeliveryDTO dto, @MappingTarget Delivery delivery);
    
    @Named("customerFromId")
    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
    
    @Named("tourFromId")
    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
    
    default String getCustomerName(Customer customer) {
        if (customer == null) {
            return null;
        }
        return customer.getName();
    }
    
    default Delivery fromId(Long id) {
        if (id == null) {
            return null;
        }
        Delivery delivery = new Delivery();
        delivery.setId(id);
        return delivery;
    }
}
