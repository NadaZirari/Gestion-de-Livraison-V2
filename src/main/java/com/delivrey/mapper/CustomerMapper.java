package com.delivrey.mapper;

import com.delivrey.dto.CustomerDto;
import com.delivrey.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    
    CustomerDto toDto(Customer customer);
    
    Customer toEntity(CustomerDto dto);
    
    /**
     * Met à jour une entité Customer à partir d'un DTO
     * @param dto Le DTO source
     * @param customer L'entité à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCustomerFromDto(CustomerDto dto, @MappingTarget Customer customer);
    
    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
