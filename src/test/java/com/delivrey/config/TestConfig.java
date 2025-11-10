package com.delivrey.config;

import com.delivrey.optimizer.ClarkeWrightOptimizer;
import com.delivrey.optimizer.NearestNeighborOptimizer;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.repository.TourRepository;
import com.delivrey.service.TourService;
import com.delivrey.service.impl.TourServiceImpl;
import com.delivrey.mapper.TourMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Qualifier;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public TourService tourService(TourRepository tourRepository,
                                 @Qualifier("nearestNeighbor") TourOptimizer nearestNeighborOptimizer,
                                 @Qualifier("clarkeWright") TourOptimizer clarkeWrightOptimizer,
                                 @Qualifier("aiOptimizer") TourOptimizer aiOptimizer) {
        return new TourServiceImpl(
            tourRepository,
            nearestNeighborOptimizer,
            clarkeWrightOptimizer,
            aiOptimizer,
            Mockito.mock(ApplicationEventPublisher.class),
            Mockito.mock(TourMapper.class)
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
