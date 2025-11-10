package com.delivrey.config;

import com.delivrey.optimizer.ClarkeWrightOptimizer;
import com.delivrey.optimizer.NearestNeighborOptimizer;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.optimizer.ai.AIOptimizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
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
    
    @Bean
    @Qualifier("aiOptimizer")
    public TourOptimizer aiOptimizer(ChatClient chatClient, ObjectMapper objectMapper) {
        return new AIOptimizer(chatClient, objectMapper);
    }
}
