package com.delivrey;

import com.delivrey.entity.Tour;
import com.delivrey.optimizer.AIOptimizer;
import com.delivrey.optimizer.ClarkeWrightOptimizer;
import com.delivrey.optimizer.NearestNeighborOptimizer;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.repository.TourRepository;
import com.delivrey.service.TourService;
import com.delivrey.service.impl.TourServiceImpl;
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
    public AIOptimizer mockAIOptimizer() {
        AIOptimizer mock = Mockito.mock(AIOptimizer.class);
        // Configure the mock to return the original tour when optimizeTour is called
        Mockito.when(mock.optimizeTour(Mockito.any(Tour.class), Mockito.any()))
               .thenAnswer(invocation -> invocation.getArgument(0));
        return mock;
    }
    
    @Bean
    public TourOptimizer nearestNeighborOptimizer() {
        return new NearestNeighborOptimizer();
    }
    
    @Bean
    public TourOptimizer clarkeWrightOptimizer() {
        return new ClarkeWrightOptimizer();
    }
    
    @Bean
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
            Mockito.mock(com.delivrey.mapper.TourMapper.class)
        );
    }
}
