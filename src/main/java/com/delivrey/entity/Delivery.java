package com.delivrey.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Represents a delivery that needs to be made to a customer.
 * Contains delivery details including location, dimensions, and status.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "delivery")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(name = "DELIVERY_ADDRESS", nullable = false)
    private String address;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(10,8)")
    private Double latitude;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(11,8)")
    private Double longitude;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double weight;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(10,3)")
    private Double volume;
    
    @Column(name = "time_window", nullable = false)
    private String timeWindow;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_STATUS", nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryHistory> deliveryHistories = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Adds a delivery history entry to this delivery.
     * @param history The delivery history to add
     */
    public void addDeliveryHistory(DeliveryHistory history) {
        deliveryHistories.add(history);
        history.setDelivery(this);
    }
    
    /**
     * Removes a delivery history entry from this delivery.
     * @param history The delivery history to remove
     */
    public void removeDeliveryHistory(DeliveryHistory history) {
        deliveryHistories.remove(history);
        history.setDelivery(null);
    }
}
