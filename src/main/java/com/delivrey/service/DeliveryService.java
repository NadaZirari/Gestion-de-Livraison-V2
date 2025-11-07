package com.delivrey.service;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;

import java.util.List;

public interface DeliveryService {
    /**
     * Récupère la liste des livraisons selon leur statut.
     * 
     * @param status le statut des livraisons à récupérer
     * @return liste des livraisons correspondant au statut
     */
    List<Delivery> getDeliveriesByStatus(DeliveryStatus status);
}
