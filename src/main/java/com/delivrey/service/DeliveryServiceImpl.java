package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;
import com.delivrey.repository.DeliveryRepository;
import com.delivrey.service.DeliveryService;

import java.util.List;

public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    // Injection via constructeur
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatus(status);
    }
}
