package com.delivrey.service;

import com.delivrey.entity.Vehicle;
import com.delivrey.repository.VehicleRepository;
import com.delivrey.service.VehicleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        Optional<Vehicle> v = vehicleRepository.findById(id);
        return v.orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        Vehicle existing = getVehicleById(id);
        existing.setRegistrationNumber(vehicle.getRegistrationNumber());
        existing.setType(vehicle.getType());
        existing.setMaxDeliveries(vehicle.getMaxDeliveries());
        existing.setMaxWeight(vehicle.getMaxWeight());
        existing.setMaxVolume(vehicle.getMaxVolume());
        return vehicleRepository.save(existing);
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
