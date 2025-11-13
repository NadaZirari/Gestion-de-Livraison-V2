package com.delivrey.dto;

import com.delivrey.entity.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.delivrey.entity.Delivery}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double weight;
    private Double volume;
    private String timeWindow;
    private DeliveryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long tourId;
}
