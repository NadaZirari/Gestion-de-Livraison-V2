package com.delivrey.service;

import com.delivrey.entity.Warehouse;
import java.util.List;

public interface WarehouseService {
    List<Warehouse> getAllWarehouses();
    Warehouse getWarehouseById(Long id);
    Warehouse createWarehouse(Warehouse warehouse);
    Warehouse updateWarehouse(Long id, Warehouse updatedWarehouse);
    void deleteWarehouse(Long id);
}
