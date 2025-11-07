package com.delivrey.controller;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.entity.Delivery;
import mapper.DeliveryMapper;
import com.delivrey.repository.DeliveryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Declare this controller as bean in XML and enable mvc:annotation-driven
 */
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @GetMapping
    @ResponseBody
    public List<DeliveryDTO> getAll() {
        return deliveryRepository.findAll().stream().map(DeliveryMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public DeliveryDTO getById(@PathVariable Long id) {
        Delivery d = deliveryRepository.findById(id).orElse(null);
        return DeliveryMapper.toDto(d);
    }

    @PostMapping
    @ResponseBody
    public DeliveryDTO create(@RequestBody DeliveryDTO dto) {
        Delivery entity = DeliveryMapper.toEntity(dto);
        Delivery saved = deliveryRepository.save(entity);
        return DeliveryMapper.toDto(saved);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public DeliveryDTO update(@PathVariable Long id, @RequestBody DeliveryDTO dto) {
        Delivery existing = deliveryRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        // map fields
        existing.setAddress(dto.getAddress());
        existing.setLatitude(dto.getLatitude());
        existing.setLongitude(dto.getLongitude());
        existing.setWeight(dto.getWeight());
        existing.setVolume(dto.getVolume());
        existing.setTimeWindow(dto.getTimeWindow());
        // status if present
        if (dto.getStatus() != null) {
            try {
                existing.setStatus(com.delivrey.entity.DeliveryStatus.valueOf(dto.getStatus()));
            } catch (IllegalArgumentException ex) { /* ignore or validate */ }
        }
        Delivery saved = deliveryRepository.save(existing);
        return DeliveryMapper.toDto(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        deliveryRepository.deleteById(id);
    }
}
