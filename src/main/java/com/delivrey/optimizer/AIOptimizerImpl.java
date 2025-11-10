package com.delivrey.optimizer;

import com.delivrey.ai.model.OptimizationResponse;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import com.delivrey.exception.OptimizationException;
import com.delivrey.repository.DeliveryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIOptimizerImpl implements AIOptimizer {

    private final ChatClient chatClient;
    @SuppressWarnings("unused") // Will be used in future updates
    private final DeliveryRepository deliveryRepository;
    private final ObjectMapper objectMapper;

    @Value("classpath:/prompts/optimization-prompt.st")
    private Resource promptTemplate;

    @Override
    @Retryable(
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2),
        retryFor = {OptimizationException.class}
    )
    @Transactional
    public Tour optimizeTour(Tour tour, List<Delivery> deliveries) {
        try {
            try {
                String prompt = buildPrompt(tour, deliveries);
                log.debug("Sending optimization request to AI: {}", prompt);
                
                // Get the response from the AI
                String response = chatClient.call(new Prompt(prompt)).getResult().getOutput().getContent();
                log.debug("Received AI response: {}", response);
                
                return parseAiResponse(tour, response);
            } catch (Exception e) {
                log.error("Error during AI optimization", e);
                throw new OptimizationException("AI optimization failed: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("AI optimization failed", e);
            throw new OptimizationException("Failed to optimize tour: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(Tour tour, List<Delivery> deliveries) throws IOException {
        String template = StreamUtils.copyToString(
            promptTemplate.getInputStream(), 
            StandardCharsets.UTF_8
        );
        
        // Create a prompt template and render it with the delivery data
        PromptTemplate promptTemplate = new PromptTemplate(template);
        String promptText = promptTemplate.create(Map.of(
            "input", toJson(deliveries),
            "vehicle", Map.of(
                "maxWeight", 1000, // Example value - should come from tour or vehicle
                "maxVolume", 10.0  // Example value - should come from tour or vehicle
            )
        ));
        
        return promptText;
    }

    private Tour parseAiResponse(Tour tour, String response) throws JsonProcessingException {
        try {
            // Parse the AI response
            OptimizationResponse optimization = objectMapper.readValue(
                response, 
                OptimizationResponse.class
            );
            
            if (optimization.getOptimizedDeliveries() == null) {
                throw new OptimizationException("AI response does not contain optimized deliveries");
            }
            
            // Log the optimization results
            log.info("Optimization completed. Total distance: {}, Vehicles used: {}", 
                optimization.getRouteSummary() != null ? optimization.getRouteSummary().getTotalDistance() : "N/A",
                optimization.getRouteSummary() != null ? optimization.getRouteSummary().getNumberOfVehiclesUsed() : 1
            );
            
            // Log recommendations and warnings if any
            if (optimization.getRecommendations() != null && !optimization.getRecommendations().isEmpty()) {
                log.info("Optimization recommendations: {}", String.join(", ", optimization.getRecommendations()));
            }
            
            if (optimization.getWarnings() != null && !optimization.getWarnings().isEmpty()) {
                log.warn("Optimization warnings: {}", String.join(", ", optimization.getWarnings()));
            }
            
            // Here you would typically update the tour with the optimized delivery order
            // For now, we'll just return the tour as is
            return tour;
            
        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response: {}", response, e);
            throw new OptimizationException("Failed to parse AI optimization response", e);
        }
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
