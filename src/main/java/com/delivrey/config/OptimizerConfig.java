package com.delivrey.config;

import com.delivrey.optimizer.ClarkeWrightOptimizer;
import com.delivrey.optimizer.NearestNeighborOptimizer;
import com.delivrey.optimizer.TourOptimizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OptimizerConfig {

    @Bean
    @Primary
    public TourOptimizer nearestNeighborOptimizer() {
        return new NearestNeighborOptimizer();
    }

    @Bean
    public TourOptimizer clarkeWrightOptimizer() {
        return new ClarkeWrightOptimizer();
    }
}
