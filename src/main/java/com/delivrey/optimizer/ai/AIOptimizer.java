package com.delivrey.optimizer.ai;

import com.delivrey.entity.Delivery;
import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.dto.ai.AIOptimizationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIOptimizer implements TourOptimizer {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    
    @Value("classpath:/prompts/optimization-prompt.st")
    private Resource optimizationPrompt;

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.size() <= 1) {
            return deliveries;
        }

        try {
            String request = createOptimizationRequest(deliveries);
            
            PromptTemplate promptTemplate = new PromptTemplate(optimizationPrompt);
            Map<String, Object> model = Map.of("deliveries", request);
            Prompt prompt = promptTemplate.create(model);
            
            String response = chatClient.call(prompt).getResult().getOutput().getContent();
            
            return processOptimizationResponse(deliveries, response);
            
        } catch (Exception e) {
            log.error("AI optimization failed, falling back to original order", e);
            return deliveries;
        }
    }

    private String createOptimizationRequest(List<Delivery> deliveries) {
        return deliveries.stream()
            .map(delivery -> String.format(
                "ID: %d, Address: %s, Lat: %.6f, Lng: %.6f, Time Window: %s",
                delivery.getId(),
                delivery.getAddress(),
                delivery.getLatitude(),
                delivery.getLongitude(),
                delivery.getTimeWindow()
            ))
            .collect(Collectors.joining("\n"));
    }

    private List<Delivery> processOptimizationResponse(List<Delivery> originalDeliveries, String aiResponse) {
        try {
            AIOptimizationResponse response = objectMapper.readValue(aiResponse, AIOptimizationResponse.class);
            
            Map<Long, Delivery> deliveryMap = originalDeliveries.stream()
                .collect(Collectors.toMap(Delivery::getId, delivery -> delivery));
            
            return response.getOptimizedDeliveries().stream()
                .map(aiDelivery -> deliveryMap.get(aiDelivery.getDeliveryId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Failed to parse AI response", e);
            return originalDeliveries;
        }
    }
}
