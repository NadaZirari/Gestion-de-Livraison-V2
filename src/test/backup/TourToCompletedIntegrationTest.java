package com.delivrey.integration;

import com.delivrey.entity.*;
import com.delivrey.repository.CustomerRepository;
import com.delivrey.repository.DeliveryHistoryRepository;
import com.delivrey.repository.DeliveryRepository;
import com.delivrey.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TourToCompletedIntegrationTest {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeliveryHistoryRepository deliveryHistoryRepository;

    private Customer testCustomer1;
    private Customer testCustomer2;
    private Vehicle testVehicle;

    @BeforeEach
    public void setUp() {
        // Créer des clients de test
        testCustomer1 = createTestCustomer("Client 1", "Adresse 1", 48.8566, 2.3522);
        testCustomer2 = createTestCustomer("Client 2", "Adresse 2", 48.8600, 2.3500);
        
        // Créer un véhicule de test
        testVehicle = new Vehicle();
        testVehicle.setRegistrationNumber("AB-123-CD");
        testVehicle.setType(VehicleType.VAN);
        testVehicle.setMaxWeight(1000.0);
        testVehicle.setMaxVolume(10.0);
    }

    @Test
    public void whenTourStatusChangedToCompleted_thenDeliveryHistoryShouldBeCreated() {
        // Given
        // Créer un tour
        Tour tour = new Tour();
        tour.setTourDate(LocalDate.now());
        tour.setTourStatus(TourStatus.PLANNED);
        tour.setVehicle(testVehicle);
        tour = tourRepository.save(tour);

        // Créer des livraisons pour le tour
        Delivery delivery1 = createTestDelivery(tour, testCustomer1, "Adresse 1");
        Delivery delivery2 = createTestDelivery(tour, testCustomer2, "Adresse 2");
        
        // Ajouter les livraisons au tour
        tour.getDeliveries().add(delivery1);
        tour.getDeliveries().add(delivery2);
        tour = tourRepository.save(tour);

        // When
        // Changer le statut du tour à COMPLETED
        tour.setTourStatus(TourStatus.COMPLETED);
        tour = tourRepository.save(tour);

        // Then
        // Vérifier que les enregistrements d'historique ont été créés
        List<DeliveryHistory> histories = deliveryHistoryRepository.findAll();
        assertEquals(2, histories.size(), "Deux enregistrements d'historique devraient être créés");
        
        // Vérifier les détails des enregistrements d'historique
        for (DeliveryHistory history : histories) {
            assertNotNull(history.getDeliveryDate(), "La date de livraison ne devrait pas être nulle");
            assertNotNull(history.getCustomer(), "Le client ne devrait pas être nul");
            assertNotNull(history.getDelivery(), "La livraison ne devrait pas être nulle");
            assertEquals(tour, history.getTour(), "Le tour devrait correspondre");
        }
    }

    private Customer createTestCustomer(String name, String address, double lat, double lng) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAddress(address);
        customer.setLatitude(lat);
        customer.setLongitude(lng);
        return customerRepository.save(customer);
    }

    private Delivery createTestDelivery(Tour tour, Customer customer, String address) {
        Delivery delivery = new Delivery();
        delivery.setCustomer(customer);
        delivery.setAddress(address);
        delivery.setLatitude(customer.getLatitude());
        delivery.setLongitude(customer.getLongitude());
        delivery.setWeight(5.0);
        delivery.setVolume(0.5);
        delivery.setTimeWindow("09:00-12:00");
        delivery.setStatus(DeliveryStatus.IN_TRANSIT);
        delivery.setTour(tour);
        return deliveryRepository.save(delivery);
    }
}
