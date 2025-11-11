package com.delivrey.controller;

import com.delivrey.entity.DeliveryHistory;
import com.delivrey.optimizer.Optimizer;
import com.delivrey.optimizer.model.OptimizationConstraints;
import com.delivrey.optimizer.model.OptimizedPlan;
import com.delivrey.service.DeliveryHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/optimize")
@RequiredArgsConstructor
@Tag(name = "Optimization", description = "API for optimizing delivery tours")
public class OptimizationController {

    private final Optimizer optimizer;
    private final DeliveryHistoryService deliveryHistoryService;

    @Operation(summary = "Optimize a delivery tour using AI")
    @ApiResponse(
        responseCode = "200", 
        description = "Tour optimized successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = OptimizedPlan.class)
        )
    )
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Tour not found")
    @ApiResponse(responseCode = "503", description = "Optimizer service unavailable")
    @PostMapping("/tour/{tourId}")
    public ResponseEntity<?> optimizeTour(
        @Parameter(description = "ID of the tour to optimize", required = true) 
        @PathVariable Long tourId,
        
        @Parameter(description = "Maximum duration in minutes")
        @RequestParam(required = false) Integer maxDuration,
        
        @Parameter(description = "Maximum deliveries per vehicle")
        @RequestParam(required = false) Integer maxDeliveries,
        
        @Parameter(description = "Consider traffic conditions")
        @RequestParam(defaultValue = "true") boolean considerTraffic
    ) {
        try {
            log.info("Optimization request received for tour: {}", tourId);
            
            // Récupérer l'historique des livraisons pour ce tour
            List<DeliveryHistory> history = deliveryHistoryService.findByTourId(tourId);
            
            // Préparer les contraintes d'optimisation
            OptimizationConstraints constraints = OptimizationConstraints.builder()
                .maxTotalDuration(maxDuration != null ? Duration.ofMinutes(maxDuration) : null)
                .maxDeliveriesPerVehicle(maxDeliveries)
                .considerTraffic(considerTraffic)
                .build();
            
            log.debug("Using optimization constraints: {}", constraints);
            
            // Vérifier la disponibilité de l'optimiseur
            if (!optimizer.isAvailable()) {
                log.warn("Optimizer is not available");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Optimizer service is currently unavailable"));
            }
            
            // Effectuer l'optimisation
            log.debug("Starting optimization with {} delivery history entries", history.size());
            OptimizedPlan plan = optimizer.optimize(history, constraints);
            
            log.info("Optimization completed successfully for tour: {}", tourId);
            return ResponseEntity.ok(plan);
            
        } catch (EntityNotFoundException e) {
            log.error("Tour not found: {}", tourId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Optimization failed for tour: " + tourId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Optimization failed: " + e.getMessage()));
        }
    }
}
