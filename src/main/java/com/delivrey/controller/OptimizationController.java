package com.delivrey.controller;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import com.delivrey.optimizer.AIOptimizer;
import com.delivrey.service.TourService;
import com.delivrey.validation.ValidOptimizationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/optimize")
@RequiredArgsConstructor
@Tag(name = "Optimization", description = "API for optimizing delivery tours")
public class OptimizationController {

    private final AIOptimizer aiOptimizer;
    private final TourService tourService;

    @Operation(summary = "Optimize a delivery tour")
    @ApiResponse(responseCode = "200", description = "Tour optimized successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Tour not found")
    @PostMapping("/tour/{tourId}")
    public ResponseEntity<?> optimizeTour(
        @Parameter(description = "ID of the tour to optimize") 
        @PathVariable Long tourId,
        
        @Parameter(description = "List of deliveries to optimize")
        @RequestBody @Valid @ValidOptimizationRequest List<Delivery> deliveries
    ) {
        try {
            Tour tour = tourService.findById(tourId)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found: " + tourId));
                
            Tour optimizedTour = aiOptimizer.optimizeTour(tour, deliveries);
            return ResponseEntity.ok(optimizedTour);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Optimization failed: " + e.getMessage()));
        }
    }
}
