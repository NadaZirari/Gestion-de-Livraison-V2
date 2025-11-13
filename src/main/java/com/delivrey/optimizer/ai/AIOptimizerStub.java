package com.delivrey.optimizer.ai;

import com.delivrey.optimizer.TourOptimizer;
import com.delivrey.entity.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("!ai")
public class AIOptimizerStub implements TourOptimizer {
    
    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries) {
        log.warn("L'optimisation par IA est désactivée. Utilisation d'un optimiseur factice qui ne fait rien.");
        return deliveries; // Retourne la liste des livraisons non modifiée
    }
}
