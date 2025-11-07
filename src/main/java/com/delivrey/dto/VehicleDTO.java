package com.delivrey.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Vehicle", description = "Représentation d'un véhicule")
public class VehicleDTO {

    @Schema(description = "Identifiant du véhicule")
    private Long id;

    @Schema(description = "Numéro d'immatriculation")
    private String registrationNumber;

    @Schema(description = "Type de véhicule")
    private String type;

    @Schema(description = "Poids maximum supporté par le véhicule")
    private double maxWeight;

    @Schema(description = "Volume maximum supporté par le véhicule")
    private double maxVolume;

    @Schema(description = "Nombre maximum de livraisons assignables")
    private int maxDeliveries;

    public VehicleDTO() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getMaxWeight() { return maxWeight; }
    public void setMaxWeight(double maxWeight) { this.maxWeight = maxWeight; }

    public double getMaxVolume() { return maxVolume; }
    public void setMaxVolume(double maxVolume) { this.maxVolume = maxVolume; }

    public int getMaxDeliveries() { return maxDeliveries; }
    public void setMaxDeliveries(int maxDeliveries) { this.maxDeliveries = maxDeliveries; }
}
