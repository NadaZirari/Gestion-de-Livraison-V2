package com.delivrey.dto;

import com.delivrey.entity.DeliveryStatus;
import com.delivrey.entity.TourStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryHistoryDto {
    private Long id;
    private Long customerId;
    private Long deliveryId;
    private Long tourId;
    private LocalDate deliveryDate;
    private LocalTime plannedTime;
    private LocalTime actualTime;
    private Integer delayMinutes;
    private DeliveryStatus deliveryStatus;
    private TourStatus tourStatus;
    private String notes;
}
