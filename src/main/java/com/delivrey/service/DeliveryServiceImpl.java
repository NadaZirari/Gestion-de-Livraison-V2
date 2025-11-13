package com.delivrey.service;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;
import com.delivrey.exception.ResourceNotFoundException;
import com.delivrey.mapper.DeliveryMapper;
import com.delivrey.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the DeliveryService interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryDTO> getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .map(deliveryMapper::toDto);
    }

    @Override
    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDTO);
        if (delivery.getStatus() == null) {
            delivery.setStatus(DeliveryStatus.PENDING);
        }
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    @Override
    public DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO) {
        return deliveryRepository.findById(id)
            .map(existingDelivery -> {
                deliveryMapper.updateDeliveryFromDto(deliveryDTO, existingDelivery);
                Delivery updatedDelivery = deliveryRepository.save(existingDelivery);
                return deliveryMapper.toDto(updatedDelivery);
            })
            .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
    }

    @Override
    public void deleteDelivery(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Delivery not found with id: " + id);
        }
        deliveryRepository.deleteById(id);
    }

    @Override
    public DeliveryDTO updateDeliveryStatus(Long id, DeliveryStatus status) {
        return deliveryRepository.findById(id)
            .map(delivery -> {
                delivery.setStatus(status);
                Delivery updatedDelivery = deliveryRepository.save(delivery);
                return deliveryMapper.toDto(updatedDelivery);
            })
            .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveriesByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatus(status).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryDTO> getDeliveriesByStatus(DeliveryStatus status, Pageable pageable) {
        Page<Delivery> page = deliveryRepository.findByStatus(status, pageable);
        List<DeliveryDTO> dtos = page.getContent().stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveriesByCustomerId(Long customerId) {
        return deliveryRepository.findByCustomerId(customerId).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> findNearbyDeliveries(double latitude, double longitude, double radiusKm, DeliveryStatus status) {
        return deliveryRepository.findNearbyDeliveries(latitude, longitude, radiusKm, status).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveriesByTimeWindow(String timeWindow) {
        return deliveryRepository.findByTimeWindow(timeWindow).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveriesCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveriesByWeightRange(Double minWeight, Double maxWeight) {
        return deliveryRepository.findByWeightBetween(minWeight, maxWeight).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveriesByVolumeRange(Double minVolume, Double maxVolume) {
        return deliveryRepository.findByVolumeBetween(minVolume, maxVolume).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(DeliveryStatus status) {
        return deliveryRepository.countByStatus(status);
    }
}
