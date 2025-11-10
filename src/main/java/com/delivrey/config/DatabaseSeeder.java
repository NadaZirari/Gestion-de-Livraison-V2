package com.delivrey.config;

import com.delivrey.entity.*;
import com.delivrey.repository.*;
import com.delivrey.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test") // Ne s'exécute pas pendant les tests
public class DatabaseSeeder implements CommandLineRunner {

    private final WarehouseRepository warehouseRepository;
    private final VehicleRepository vehicleRepository;
    private final DeliveryRepository deliveryRepository;
    private final TourRepository tourRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public DatabaseSeeder(WarehouseRepository warehouseRepository,
                         VehicleRepository vehicleRepository,
                         DeliveryRepository deliveryRepository,
                         TourRepository tourRepository,
                         CustomerRepository customerRepository) {
        this.warehouseRepository = warehouseRepository;
        this.vehicleRepository = vehicleRepository;
        this.deliveryRepository = deliveryRepository;
        this.tourRepository = tourRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Vérifier si des données existent déjà
        if (warehouseRepository.count() == 0) {
            List<Warehouse> warehouses = seedWarehouses();
            List<Vehicle> vehicles = seedVehicles();
            List<Delivery> deliveries = seedDeliveries(warehouses.get(0));
            seedTours(vehicles.get(0), deliveries);
        }
    }

    private List<Warehouse> seedWarehouses() {
        Warehouse mainWarehouse = new Warehouse();
        mainWarehouse.setAddress("123 Rue Principale, 75000 Paris");
        mainWarehouse.setLatitude(48.8566);
        mainWarehouse.setLongitude(2.3522);
        mainWarehouse.setOpeningHours("08:00-20:00");

        Warehouse secondWarehouse = new Warehouse();
        secondWarehouse.setAddress("456 Avenue des Champs, 69000 Lyon");
        secondWarehouse.setLatitude(45.7640);
        secondWarehouse.setLongitude(4.8357);
        secondWarehouse.setOpeningHours("09:00-19:00");

        return warehouseRepository.saveAll(Arrays.asList(mainWarehouse, secondWarehouse));
    }

    private List<Vehicle> seedVehicles() {
        Vehicle van = new Vehicle();
        van.setRegistrationNumber("AB-123-CD");
        van.setType(VehicleType.VAN);
        van.setMaxWeight(1000.0);
        van.setMaxVolume(10.0);

        Vehicle truck = new Vehicle();
        truck.setRegistrationNumber("EF-456-GH");
        truck.setType(VehicleType.TRUCK);
        truck.setMaxWeight(5000.0);
        truck.setMaxVolume(30.0);

        Vehicle bike = new Vehicle();
        bike.setRegistrationNumber("IJ-789-KL");
        bike.setType(VehicleType.BIKE);
        bike.setMaxWeight(50.0);
        bike.setMaxVolume(0.5);

        return vehicleRepository.saveAll(Arrays.asList(van, truck, bike));
    }

    private List<Delivery> seedDeliveries(Warehouse warehouse) {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setAddress("123 Main St, Paris");
        customer.setLatitude(48.8566);
        customer.setLongitude(2.3522);
        customer.setPreferredTimeSlot("09:00-18:00");
        LocalDateTime now = LocalDateTime.now();
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);
        customer = customerRepository.save(customer);

        // Create deliveries
        Delivery delivery1 = new Delivery();
        delivery1.setCustomer(customer);
        delivery1.setAddress("10 Rue de la Paix, 75001 Paris");
        delivery1.setLatitude(48.8698);
        delivery1.setLongitude(2.3073);
        delivery1.setStatus(DeliveryStatus.PENDING);
        delivery1.setWeight(5.0);
        delivery1.setVolume(0.5);
        delivery1.setTimeWindow("09:00-12:00");

        Delivery delivery2 = new Delivery();
        delivery2.setCustomer(customer);
        delivery2.setAddress("20 Avenue des Champs-Élysées, 75008 Paris");
        delivery2.setLatitude(48.8709);
        delivery2.setLongitude(2.3030);
        delivery2.setStatus(DeliveryStatus.PENDING);
        delivery2.setWeight(2.5);
        delivery2.setVolume(0.3);
        delivery2.setTimeWindow("14:00-17:00");

        List<Delivery> deliveries = Arrays.asList(delivery1, delivery2);
        return deliveryRepository.saveAll(deliveries);
    }

    private void seedTours(Vehicle vehicle, List<Delivery> deliveries) {
        // Create a new tour
        Tour tour = new Tour();
        tour.setTourDate(LocalDate.now().plusDays(1));
        tour.setVehicle(vehicle);
        
        // Save the tour first to get an ID
        Tour savedTour = tourRepository.save(tour);
        
        // Update each delivery with the tour
        for (Delivery delivery : deliveries) {
            delivery.setTour(savedTour);
        }
        
        // Save the updated deliveries
        deliveryRepository.saveAll(deliveries);
    }
}
