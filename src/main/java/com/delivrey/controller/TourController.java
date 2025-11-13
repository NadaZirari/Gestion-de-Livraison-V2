package com.delivrey.controller;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.TourStatus;
import com.delivrey.exception.EntityNotFoundException;
import com.delivrey.mapper.DeliveryMapper;
import com.delivrey.mapper.TourMapper;
import com.delivrey.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing tours.
 */
@Slf4j
@RestController
@RequestMapping("/api/tours")
@Tag(name = "Tours", description = "API for managing tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;
    private final TourMapper tourMapper;

    @Operation(summary = "Optimize a tour", description = "Returns an optimized list of deliveries for a tour")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully optimized the tour",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = DeliveryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tour not found")
    })
    @GetMapping("/{tourId}/optimize")
    public ResponseEntity<List<DeliveryDTO>> optimize(
            @Parameter(description = "ID of the tour to optimize", required = true)
            @PathVariable("tourId") Long tourId,
            @Parameter(description = "Algorithm to use for optimization (NN, CLARKE_WRIGHT, AI)", 
                      example = "NN")
            @RequestParam(name = "algo", defaultValue = "NN") String algo) {
        
        log.info("Optimizing tour {} with algorithm {}", tourId, algo);
        List<Delivery> optimized = tourService.getOptimizedTour(tourId, algo);
        List<DeliveryDTO> result = optimized.stream()
                .map(DeliveryMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get total distance of a tour", 
              description = "Returns the total distance of a tour using the specified algorithm")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully calculated distance",
                   content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Tour not found")
    })
    @GetMapping("/{tourId}/distance")
    public ResponseEntity<Double> getDistance(
            @Parameter(description = "ID of the tour", required = true)
            @PathVariable("tourId") Long tourId,
            @Parameter(description = "Algorithm to use for distance calculation (NN, CLARKE_WRIGHT, AI)", 
                      example = "NN")
            @RequestParam(name = "algo", defaultValue = "NN") String algo) {
        
        log.info("Calculating distance for tour {} with algorithm {}", tourId, algo);
        double distance = tourService.getTotalDistance(tourId, algo);
        return ResponseEntity.ok(distance);
    }

    @Operation(summary = "Get a tour by ID", description = "Returns a single tour by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tour found",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = TourDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tour not found")
    })
    @GetMapping("/{tourId}")
    public ResponseEntity<TourDTO> getTourById(
            @Parameter(description = "ID of the tour to retrieve", required = true)
            @PathVariable Long tourId) {
        
        log.debug("REST request to get Tour : {}", tourId);
        TourDTO tourDTO = tourService.getTourById(tourId);
        return ResponseEntity.ok(tourDTO);
    }
    
    @Operation(summary = "Mark a tour as completed", description = "Marks a tour as completed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tour marked as completed",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = TourDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tour not found")
    })
    @PutMapping("/{tourId}/complete")
    @Transactional
    public ResponseEntity<TourDTO> completeTour(
            @Parameter(description = "ID of the tour to mark as completed", required = true)
            @PathVariable Long tourId) {
        
        log.debug("REST request to complete Tour : {}", tourId);
        TourDTO updatedTour = tourService.updateTourStatus(tourId, TourStatus.COMPLETED);
        return ResponseEntity.ok(updatedTour);
    }

    @Operation(summary = "Get all tours", description = "Returns a list of all tours")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tours",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = TourDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TourDTO>> getAllTours() {
        log.debug("REST request to get all Tours");
        List<TourDTO> tours = tourService.getAllTours();
        return ResponseEntity.ok(tours);
    }

    @Operation(summary = "Get all tours with pagination", description = "Returns a paginated list of tours")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated tours",
                   content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/page")
    public ResponseEntity<Page<TourDTO>> getAllTours(Pageable pageable) {
        log.debug("REST request to get a page of Tours");
        Page<TourDTO> page = tourService.getAllTours(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Create a tour", description = "Creates a new tour")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tour created successfully",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = TourDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @Transactional
    public ResponseEntity<TourDTO> createTour(
            @Parameter(description = "Tour to create", required = true)
            @Valid @RequestBody TourDTO tourDTO) {
        
        log.debug("REST request to save Tour : {}", tourDTO);
        TourDTO result = tourService.createTour(tourDTO);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
                
        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "Update a tour", description = "Updates an existing tour")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tour updated successfully",
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = TourDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Tour not found")
    })
    @PutMapping("/{tourId}")
    @Transactional
    public ResponseEntity<TourDTO> updateTour(
            @Parameter(description = "ID of the tour to update", required = true)
            @PathVariable Long tourId,
            @Parameter(description = "Updated tour data", required = true)
            @Valid @RequestBody TourDTO tourDTO) {
        
        log.debug("REST request to update Tour : {}, {}", tourId, tourDTO);
        TourDTO result = tourService.updateTour(tourId, tourDTO);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete a tour", description = "Deletes a tour by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tour deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tour not found")
    })
    @DeleteMapping("/{tourId}")
    @Transactional
    public ResponseEntity<Void> deleteTour(
            @Parameter(description = "ID of the tour to delete", required = true)
            @PathVariable Long tourId) {
        
        log.debug("REST request to delete Tour : {}", tourId);
        tourService.deleteTour(tourId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Get tours by status", description = "Returns a list of tours filtered by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tours by status",
                   content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TourDTO>> getToursByStatus(
            @Parameter(description = "Status to filter by", required = true)
            @PathVariable TourStatus status) {
        
        log.debug("REST request to get Tours by status: {}", status);
        List<TourDTO> tours = tourService.findByStatus(status);
        return ResponseEntity.ok(tours);
    }
    
    @Operation(summary = "Get tours by date range", description = "Returns a list of tours within the specified date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tours by date range",
                   content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<TourDTO>> getToursByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true, example = "2023-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true, example = "2023-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        
        log.debug("REST request to get Tours between {} and {}", startDate, endDate);
        List<TourDTO> tours = tourService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(tours);
    }
}
