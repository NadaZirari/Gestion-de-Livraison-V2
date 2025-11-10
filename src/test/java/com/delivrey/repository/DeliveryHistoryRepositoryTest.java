package com.delivrey.repository;

import com.delivrey.entity.Customer;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.DeliveryHistory;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class DeliveryHistoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeliveryHistoryRepository deliveryHistoryRepository;

    private Customer testCustomer;
    private Delivery testDelivery;
    private Tour testTour;
    private DeliveryHistory testHistory;

    @BeforeEach
    void setUp() {
        // Create test data
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        entityManager.persist(testCustomer);

        testTour = new Tour();
        testTour.setTourStatus(TourStatus.COMPLETED);
        entityManager.persist(testTour);

        testDelivery = new Delivery();
        testDelivery.setCustomer(testCustomer);
        testDelivery.setTour(testTour);
        entityManager.persist(testDelivery);

        testHistory = new DeliveryHistory();
        testHistory.setCustomer(testCustomer);
        testHistory.setDelivery(testDelivery);
        testHistory.setTour(testTour);
        testHistory.setDeliveryDate(LocalDate.now());
        testHistory.setPlannedTime(LocalTime.of(14, 0));
        testHistory.setActualTime(LocalTime.of(14, 15));
        testHistory.setDelayMinutes(15);
        testHistory.setDayOfWeek(java.time.DayOfWeek.MONDAY);
        entityManager.persist(testHistory);
        
        entityManager.flush();
    }

    @Test
    void findByCustomerId_shouldReturnHistories() {
        List<DeliveryHistory> found = deliveryHistoryRepository.findByCustomerId(testCustomer.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(testHistory.getId());
    }

    @Test
    void findByTourId_shouldReturnHistories() {
        List<DeliveryHistory> found = deliveryHistoryRepository.findByTourId(testTour.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(testHistory.getId());
    }

    @Test
    void findByDeliveryDateBetween_shouldReturnHistories() {
        LocalDate today = LocalDate.now();
        List<DeliveryHistory> found = deliveryHistoryRepository.findByDeliveryDateBetween(
            today.minusDays(1), 
            today.plusDays(1)
        );
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(testHistory.getId());
    }

    @Test
    void findDelayedDeliveries_shouldReturnDelayed() {
        List<DeliveryHistory> found = deliveryHistoryRepository.findDelayedDeliveries();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getDelayMinutes()).isGreaterThan(0);
    }

    @Test
    void findDeliveriesByTimeWindow_shouldReturnMatching() {
        List<DeliveryHistory> found = deliveryHistoryRepository.findDeliveriesByTimeWindow(
            LocalTime.of(13, 0), 
            LocalTime.of(15, 0)
        );
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(testHistory.getId());
    }
}
