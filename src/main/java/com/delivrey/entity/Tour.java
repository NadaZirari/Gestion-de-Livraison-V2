package com.delivrey.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate tourDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "TOUR_STATUS", nullable = false)
    private TourStatus tourStatus = TourStatus.PLANNED;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vehicle vehicle;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getTourDate() {
		return tourDate;
	}

	public void setTourDate(LocalDate tourDate) {
		this.tourDate = tourDate;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public String getAlgorithmUsed() {
		return algorithmUsed;
	}

	public void setAlgorithmUsed(String algorithmUsed) {
		this.algorithmUsed = algorithmUsed;
	}

    public TourStatus getTourStatus() {
        return tourStatus;
    }

    public void setTourStatus(TourStatus tourStatus) {
        this.tourStatus = tourStatus;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Warehouse warehouse;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tour_delivery",
        joinColumns = @JoinColumn(name = "tour_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_id")
    )
    @JsonIgnoreProperties("tours")
    private List<Delivery> deliveries;

    private String algorithmUsed;

    public Tour() {}

    // Getters, Setters
}
