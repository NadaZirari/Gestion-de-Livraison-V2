package com.delivrey.controller;

import com.delivrey.dto.DeliveryHistoryDto;
import com.delivrey.service.DeliveryHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/delivery-histories")
@Tag(name = "Delivery History API", description = "API pour l'historique des livraisons")
public class DeliveryHistoryController {

    private final DeliveryHistoryService deliveryHistoryService;

    public DeliveryHistoryController(@NonNull DeliveryHistoryService deliveryHistoryService) {
        this.deliveryHistoryService = deliveryHistoryService;
    }

    @GetMapping
    @Operation(summary = "Récupère tous les historiques de livraison avec pagination et filtres")
    public ResponseEntity<Page<DeliveryHistoryDto>> getAllDeliveryHistories(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long tourId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @PageableDefault(size = 20) @NonNull Pageable pageable) {
        
        if (customerId != null) {
            return ResponseEntity.ok(deliveryHistoryService.findByCustomerId(customerId, pageable));
        } else if (tourId != null) {
            return ResponseEntity.ok(deliveryHistoryService.findByTourId(tourId, pageable));
        } else if (fromDate != null && toDate != null) {
            return ResponseEntity.ok(deliveryHistoryService.findByDeliveryDateBetween(fromDate, toDate, pageable));
        }
        
        return ResponseEntity.ok(deliveryHistoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un historique de livraison par son ID")
    public ResponseEntity<DeliveryHistoryDto> getDeliveryHistoryById(@PathVariable @NonNull Long id) {
        return deliveryHistoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}/stats")
    @Operation(summary = "Récupère les statistiques de livraison pour un client")
    public ResponseEntity<?> getCustomerDeliveryStats(@PathVariable @NonNull Long customerId) {
        return ResponseEntity.ok(deliveryHistoryService.getCustomerDeliveryStats(customerId));
    }

    @GetMapping("/tour/{tourId}/stats")
    @Operation(summary = "Récupère les statistiques de livraison pour un tour")
    public ResponseEntity<?> getTourDeliveryStats(@PathVariable @NonNull Long tourId) {
        return ResponseEntity.ok(deliveryHistoryService.getTourDeliveryStats(tourId));
    }
}
