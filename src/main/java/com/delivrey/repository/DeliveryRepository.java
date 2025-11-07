package com.delivrey.repository;

import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	
	
	 List<Delivery> findByStatus(DeliveryStatus pending);
}
