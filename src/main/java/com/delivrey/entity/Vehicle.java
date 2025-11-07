package com.delivrey.entity;

import com.delivrey.entity.VehicleType;
import jakarta.persistence.*;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public VehicleType getType() {
		return type;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public double getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(double maxVolume) {
		this.maxVolume = maxVolume;
	}

	public int getMaxDeliveries() {
		return maxDeliveries;
	}

	public void setMaxDeliveries(int maxDeliveries) {
		this.maxDeliveries = maxDeliveries;
	}

	private double maxWeight;
    private double maxVolume;
    private int maxDeliveries;

    public Vehicle() {}

    // Getters, Setters
}
