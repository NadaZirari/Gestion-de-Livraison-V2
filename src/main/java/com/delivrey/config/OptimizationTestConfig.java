package com.delivrey.config;

import com.delivrey.dto.DeliveryHistoryDto;
import com.delivrey.entity.Delivery;
import com.delivrey.optimizer.Optimizer;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.optimizer.model.OptimizationConstraints;
import com.delivrey.optimizer.model.OptimizedPlan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Configuration
@Profile("!cloud")
public class OptimizationTestConfig {

    @Bean
    @Primary
    public Optimizer optimizer() {
        return new Optimizer() {
            @Override
            public OptimizedPlan optimize(List<DeliveryHistoryDto> history, OptimizationConstraints constraints) {
                // Return a simple mock plan
                return OptimizedPlan.builder()
                        .orderedDeliveries(List.of()) // Empty list of deliveries
                        .recommendations(List.of("This is a mock optimization result"))
                        .predictedRoutes(List.of()) // Empty list of routes
                        .metadata(OptimizedPlan.OptimizationMetadata.builder()
                                .optimizerName("MockOptimizer")
                                .optimizerVersion("1.0.0")
                                .optimizationTime(LocalDateTime.now())
                                .optimizationDuration(Duration.ofMillis(100))
                                .totalDeliveries(history != null ? history.size() : 0)
                                .totalDistance(0.0)
                                .totalDuration(Duration.ZERO)
                                .statistics(Map.of("status", "SUCCESS"))
                                .constraintsUsed(Map.of())
                                .build())
                        .build();
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            @Override
            public String getOptimizerName() {
                return "MockOptimizer";
            }
        };
    }

    @Bean
    @Qualifier("aiOptimizer")
    public TourOptimizer aiOptimizer() {
        return new TourOptimizer() {
            @Override
            public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
                // Return the same list as a simple mock implementation
                return deliveries;
            }
        };
    }

    // Clarke-Wright Optimizer
    @Bean
    @Qualifier("clarkeWright")
    public TourOptimizer clarkeWrightOptimizer() {
        return new TourOptimizer() {
            @Override
            public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
                // Return the same list as a simple mock implementation
                return deliveries;
            }
        };
    }

    // Nearest Neighbor Optimizer
    @Bean
    @Qualifier("nearestNeighbor")
    public TourOptimizer nearestNeighborOptimizer() {
        return new TourOptimizer() {
            @Override
            public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
                // Return the same list as a simple mock implementation
                return deliveries;
            }
        };
    }
    
}
