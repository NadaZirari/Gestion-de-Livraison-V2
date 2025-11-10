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
            String prompt = buildPrompt(tour, deliveries);
            log.debug("Sending optimization request to AI: {}", prompt);
            
            // Get the response from the AI
            String response = chatClient.call(prompt);
            log.debug("Received AI response: {}", response);
            
            return parseAiResponse(tour, response);
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
        
        // Create a map of variables for the template
        Map<String, Object> variables = Map.of(
            "input", toJson(deliveries),
            "vehicle", Map.of(
                "maxWeight", 1000, // Example value - should come from tour or vehicle
                "maxVolume", 10.0  // Example value - should come from tour or vehicle
            )
        );
        
        // Create and process the template
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(variables);
        
        // Return the rendered template as a string
        return prompt.getContents();
    }

    private Tour parseAiResponse(Tour tour, String aiResponse) {
        try {
            OptimizationResponse response = objectMapper.readValue(aiResponse, OptimizationResponse.class);
            // Update tour with optimized deliveries
            // This is a simplified example - you'll need to implement the actual mapping
            // based on your business logic and entity relationships
            if (response.getOptimizedDeliveries() != null) {
                // Update delivery order, estimated times, etc.
                // This is just a placeholder - implement according to your needs
                response.getOptimizedDeliveries().forEach(delivery -> {
                    // Update delivery order and other properties
                });
            }
            return tour;
        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response", e);
            throw new OptimizationException("Failed to parse AI response", e);
        }
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON", e);
            throw new OptimizationException("Failed to convert object to JSON", e);
        }
    }
}
