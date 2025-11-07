package com.delivrey.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Tour", description = "Représentation d'un tour de livraison")
public class TourDTO {

    @Schema(description = "Identifiant du tour")
    private Long id;

    @Schema(description = "Date du tour")
    private LocalDate date;

    @Schema(description = "Statut du tour (ex : PLANNED, IN_PROGRESS, COMPLETED)")
    private String status;

    @Schema(description = "Liste des livraisons associées au tour")
    private List<DeliveryDTO> deliveries;

    @Schema(description = "Identifiant du véhicule assigné au tour")
    private Long vehicleId;

    @Schema(description = "Identifiant du conducteur assigné au tour")
    private Long driverId;

    @Schema(description = "Identifiant de l'entrepôt de départ")
    private Long warehouseId;

    @Schema(description = "Identifiant de l'itinéraire associé au tour")
    private Long routeId;

    @Schema(description = "Distance totale du tour")
    private double totalDistance;

    public TourDTO() {}

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<DeliveryDTO> getDeliveries() { return deliveries; }
    public void setDeliveries(List<DeliveryDTO> deliveries) { this.deliveries = deliveries; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
}
