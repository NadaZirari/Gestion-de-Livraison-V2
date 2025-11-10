package com.delivrey.dto;

import com.delivrey.entity.TourStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "Tour", description = "Représentation d'un tour de livraison")
public class TourDTO {

    @Schema(description = "Identifiant du tour")
    private Long id;

    @Schema(description = "Date du tour")
    private LocalDate date;

    @Schema(description = "Statut du tour (ex : PLANNED, IN_PROGRESS, COMPLETED)")
    private TourStatus status;

    @Schema(description = "Liste des identifiants des livraisons associées au tour")
    private List<Long> deliveryIds;

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
    
    @Schema(description = "Algorithme utilisé pour l'optimisation")
    private String algorithmUsed;

    public TourDTO() {}

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public TourStatus getStatus() { return status; }
    public void setStatus(TourStatus status) { this.status = status; }

    public List<Long> getDeliveryIds() { return deliveryIds; }
    public void setDeliveryIds(List<Long> deliveryIds) { this.deliveryIds = deliveryIds; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }

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
