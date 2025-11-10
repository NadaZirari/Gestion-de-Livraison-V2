package com.delivrey.config;

import com.delivrey.optimizer.ClarkeWrightOptimizer;
import com.delivrey.optimizer.NearestNeighborOptimizer;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.repository.TourRepository;
import com.delivrey.service.DeliveryHistoryService;
import com.delivrey.service.TourService;
import com.delivrey.service.impl.TourServiceImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public TourService tourService(TourRepository tourRepository,
                                 NearestNeighborOptimizer nearestNeighborOptimizer,
                                 ClarkeWrightOptimizer clarkeWrightOptimizer) {
        return new TourServiceImpl(
            tourRepository,
            nearestNeighborOptimizer,
            clarkeWrightOptimizer,
            Mockito.mock(ThreadPoolTaskExecutor.class),
            Mockito.mock(DeliveryHistoryService.class)
        );
    }
    
    @Bean
    public TourOptimizer mockNearestNeighborOptimizer() {
        return Mockito.mock(NearestNeighborOptimizer.class);
    }
    
    @Bean
    public TourOptimizer mockClarkeWrightOptimizer() {
        return Mockito.mock(ClarkeWrightOptimizer.class);
    }
}
