package com.delivrey.controller;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Contrôleur pour la gestion des livraisons
 */
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);
    
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DeliveryDTO>> getAll() {
        logger.info("Fetching all deliveries");
        try {
            List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries();
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            logger.error("Error fetching deliveries: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DeliveryDTO> getById(@PathVariable("id") Long id) {
        logger.info("Fetching delivery with id: {}", id);
        try {
            return deliveryService.getDeliveryById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error fetching delivery with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DeliveryDTO> create(@Valid @RequestBody DeliveryDTO dto) {
        try {
            logger.info("Creating new delivery for address: {}", dto.getAddress());
            DeliveryDTO created = deliveryService.createDelivery(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Error creating delivery: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DeliveryDTO> update(
            @PathVariable("id") Long id, 
            @Valid @RequestBody DeliveryDTO dto) {
        
        logger.info("Updating delivery with id: {}", id);
        
        try {
            return ResponseEntity.ok(deliveryService.updateDelivery(id, dto));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                logger.warn("Attempted to update non-existent delivery with id: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.error("Error updating delivery with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        logger.info("Deleting delivery with id: {}", id);
        try {
            deliveryService.deleteDelivery(id);
            logger.info("Successfully deleted delivery with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                logger.warn("Attempted to delete non-existent delivery with id: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.error("Error deleting delivery with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
