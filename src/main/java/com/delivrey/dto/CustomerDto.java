package com.delivrey.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CustomerDto {
    private Long id;
    
    @NotNull(message = "Le nom est obligatoire")
    private String name;
    
    @NotNull(message = "L'adresse est obligatoire")
    private String address;
    
    @NotNull(message = "La latitude est obligatoire")
    private Double latitude;
    
    @NotNull(message = "La longitude est obligatoire")
    private Double longitude;
    
    private String preferredTimeSlot; // Format: "09:00-11:00"
}
