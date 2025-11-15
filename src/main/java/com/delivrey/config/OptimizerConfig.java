package com.delivrey.config;

import com.delivrey.optimizer.ClarkeWrightOptimizer;
import com.delivrey.optimizer.NearestNeighborOptimizer;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.optimizer.ai.AIOptimizerStub;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(OptimizerProperties.class)
@Profile("!test") // Ne pas charger cette configuration en mode test
public class OptimizerConfig {

    @Bean
    @Primary
    @Qualifier("nearestNeighbor")
    public TourOptimizer nearestNeighbor() {
        return new NearestNeighborOptimizer();
    }

    @Bean
    @Qualifier("clarkeWright")
    public TourOptimizer clarkeWright() {
        return new ClarkeWrightOptimizer();
    }
    
    @Bean
    @Qualifier("aiOptimizer")
    public TourOptimizer aiOptimizer() {
        return new AIOptimizerStub();
    }
}
