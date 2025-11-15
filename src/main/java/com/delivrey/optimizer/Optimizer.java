
package com.delivrey.optimizer;

import com.delivrey.dto.DeliveryHistoryDto;
import com.delivrey.optimizer.model.OptimizedPlan;
import com.delivrey.optimizer.model.OptimizationConstraints;

import java.util.List;

/**
 * Interface pour les algorithmes d'optimisation des tournées de livraison.
 */
public interface Optimizer {
    
    /**
     * Optimise une liste de livraisons selon les contraintes données.
     *
     * @param history Liste des historiques de livraison pour analyse
     * @param constraints Contraintes d'optimisation
     * @return Un plan d'optimisation contenant les livraisons ordonnées et les recommandations
     */
    OptimizedPlan optimize(List<DeliveryHistoryDto> history, OptimizationConstraints constraints);
    
    /**
     * Vérifie si l'optimiseur est disponible pour le moment.
     * 
     * @return true si l'optimiseur est disponible, false sinon
     */
    boolean isAvailable();
    
    /**
     * Retourne le nom de l'optimiseur.
     * 
     * @return Le nom de l'optimiseur
     */
    String getOptimizerName();
}
