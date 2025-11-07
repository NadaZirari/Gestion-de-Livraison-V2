package com.delivrey.repository;

import com.delivrey.entity.Warehouse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	
	
	List<Warehouse> findByAddress(String address);
}
