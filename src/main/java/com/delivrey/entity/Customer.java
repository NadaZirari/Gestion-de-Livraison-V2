package com.delivrey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(10,8)")
    private Double latitude;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(11,8)")
    private Double longitude;
    
    @Column(name = "preferred_time_slot")
    private String preferredTimeSlot; // Format: "09:00-11:00"
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    // Helper method to add delivery
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        delivery.setCustomer(this);
    }
    
    // Helper method to remove delivery
    public void removeDelivery(Delivery delivery) {
        deliveries.remove(delivery);
        delivery.setCustomer(null);
    }
}
