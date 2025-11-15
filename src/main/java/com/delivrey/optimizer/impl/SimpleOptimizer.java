package com.delivrey.optimizer.impl;

import com.delivrey.dto.DeliveryHistoryDto;
import com.delivrey.optimizer.Optimizer;
import com.delivrey.optimizer.model.OptimizationConstraints;
import com.delivrey.optimizer.model.OptimizedPlan;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implémentation simple de l'optimiseur pour le développement et les tests.
 * Cette implémentation ne fait pas d'optimisation réelle mais permet de faire fonctionner l'application.
 */
@Component
@Profile("!test") // Ne pas utiliser en environnement de test
public class SimpleOptimizer implements Optimizer {

    @Override
    public OptimizedPlan optimize(List<DeliveryHistoryDto> history, OptimizationConstraints constraints) {
        // Créer un plan d'optimisation factice
        return OptimizedPlan.builder()
                .orderedDeliveries(Collections.emptyList()) // Aucune livraison réordonnée
                .recommendations(List.of("Optimisation désactivée - Mode développement"))
                .predictedRoutes(Collections.emptyList())
                .metadata(OptimizedPlan.OptimizationMetadata.builder()
                        .optimizerName("SimpleOptimizer")
                        .optimizerVersion("1.0.0")
                        .optimizationTime(LocalDateTime.now())
                        .optimizationDuration(Duration.ZERO)
                        .totalDeliveries(history != null ? history.size() : 0)
                        .totalDistance(0.0)
                        .totalDuration(Duration.ZERO)
                        .statistics(Map.of("status", "SUCCESS", "message", "Optimisation désactivée"))
                        .constraintsUsed(Map.of())
                        .build())
                .build();
    }

    @Override
    public boolean isAvailable() {
        return true; // Toujours disponible
    }

    @Override
    public String getOptimizerName() {
        return "SimpleOptimizer";
    }
}
