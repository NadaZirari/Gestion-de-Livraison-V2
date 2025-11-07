package com.delivrey.service;

import com.delivrey.entity.Warehouse;
import com.delivrey.repository.WarehouseRepository;
import com.delivrey.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;

    // Injection via constructeur
    public WarehouseServiceImpl(WarehouseRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return repository.findAll();
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
    }

    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {
        return repository.save(warehouse);
    }

    @Override
    public Warehouse updateWarehouse(Long id, Warehouse updatedWarehouse) {
        Warehouse existing = getWarehouseById(id);
        existing.setAddress(updatedWarehouse.getAddress());
        existing.setLatitude(updatedWarehouse.getLatitude());
        existing.setLongitude(updatedWarehouse.getLongitude());
        existing.setOpeningHours(updatedWarehouse.getOpeningHours());
        return repository.save(existing);
    }

    @Override
    public void deleteWarehouse(Long id) {
        repository.deleteById(id);
    }
}
