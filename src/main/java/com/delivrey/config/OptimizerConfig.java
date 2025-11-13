package com.delivrey.config;

import com.delivrey.optimizer.ClarkeWrightOptimizer;
import com.delivrey.optimizer.NearestNeighborOptimizer;
import com.delivrey.optimizer.TourOptimizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(OptimizerProperties.class)
public class OptimizerConfig {

    @Bean
    @Primary
    @Qualifier("nearestNeighbor")
    public TourOptimizer nearestNeighborOptimizer() {
        return new NearestNeighborOptimizer();
    }

    @Bean
    @Qualifier("clarkeWright")
    public TourOptimizer clarkeWrightOptimizer() {
        return new ClarkeWrightOptimizer();
    }
    
    // L'optimiseur IA a été désactivé pour le moment
}
