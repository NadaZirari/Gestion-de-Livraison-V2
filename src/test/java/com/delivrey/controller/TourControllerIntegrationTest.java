

package com.delivrey.controller;

import com.delivrey.entity.Customer;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryStatus;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import com.delivrey.entity.Vehicle;
import com.delivrey.entity.VehicleType;
import com.delivrey.repository.CustomerRepository;
import com.delivrey.repository.DeliveryHistoryRepository;
import com.delivrey.repository.DeliveryRepository;
import com.delivrey.repository.TourRepository;
import com.delivrey.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")  // Use H2 in-memory database for tests
public class TourControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeliveryHistoryRepository deliveryHistoryRepository;

    @Autowired
    private VehicleRepository vehicleRepository;


    @BeforeEach
    void setUp() {
        // Clear all data before each test
        deliveryHistoryRepository.deleteAll();
        deliveryRepository.deleteAll();
        tourRepository.deleteAll();
        customerRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    private Customer createTestCustomer() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setAddress("123 Test St");
        customer.setLatitude(34.0522);
        customer.setLongitude(-118.2437);
        customer.setPreferredTimeSlot("09:00-17:00");
        return customerRepository.save(customer);
    }

    private Vehicle createTestVehicle() {
        // Create a vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber("ABC123");
        vehicle.setMaxWeight(1000.0);
        vehicle.setMaxVolume(50.0);
        vehicle.setType(VehicleType.VAN);
        return vehicleRepository.save(vehicle);
    }

    private Tour createTestTour(Customer customer) {
        Vehicle vehicle = createTestVehicle();
        
        // Create a tour
        Tour tour = new Tour();
        tour.setTourStatus(TourStatus.PLANNED);
        tour.setTourDate(LocalDate.now());
        tour.setVehicle(vehicle);
        tour = tourRepository.save(tour);

        // Create a delivery
        Delivery delivery = new Delivery();
        delivery.setCustomer(customer);
        delivery.setAddress(customer.getAddress());
        delivery.setLatitude(customer.getLatitude());
        delivery.setLongitude(customer.getLongitude());
        delivery.setWeight(10.0);
        delivery.setVolume(1.0);
        delivery.setTimeWindow("09:00-17:00");
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setTour(tour);
        deliveryRepository.save(delivery);

        return tour;
    }

    @Test
    public void completeTour_ShouldUpdateStatus() throws Exception {
        // Given
        Customer customer = createTestCustomer();
        Tour tour = createTestTour(customer);
        
        // When & Then
        mockMvc.perform(put("/api/tours/{id}/status", tour.getId())
                .param("status", "COMPLETED")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tourStatus").value("COMPLETED"));
    }

    @Test
    public void getTour_ShouldReturnTour() throws Exception {
        // Given
        Customer customer = createTestCustomer();
        Tour tour = createTestTour(customer);
        
        // When & Then
        mockMvc.perform(get("/api/tours/{id}", tour.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tour.getId()))
                .andExpect(jsonPath("$.tourStatus").value("PLANNED"));
    }
    
    @Test
    public void completeTour_WithInvalidStatus_ShouldReturnBadRequest() throws Exception {
        // Given
        Customer customer = createTestCustomer();
        Tour tour = createTestTour(customer);
        
        // When & Then
        mockMvc.perform(put("/api/tours/{id}/status", tour.getId())
                .param("status", "INVALID_STATUS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
