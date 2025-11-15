package com.delivrey.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_history")
@Accessors(chain = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class DeliveryHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
    
    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;
    
    @Column(name = "planned_time", nullable = false)
    private LocalTime plannedTime;
    
    @Column(name = "actual_time")
    private LocalTime actualTime;
    
    @Column(name = "delay_minutes")
    private Integer delayMinutes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
    
    // Calculate delay in minutes
    public void calculateDelay() {
        if (this.actualTime != null && this.plannedTime != null) {
            long delay = java.time.Duration.between(this.plannedTime, this.actualTime).toMinutes();
            this.delayMinutes = delay > 0 ? (int) delay : 0;
        }
    }
    
    // Set day of week from delivery date
    @PrePersist
    @PreUpdate
    private void setDayOfWeek() {
        if (this.deliveryDate != null) {
            this.dayOfWeek = this.deliveryDate.getDayOfWeek();
        }
    }
}
