package com.delivrey.validation;

import com.delivrey.entity.Delivery;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class OptimizationRequestValidator 
    implements ConstraintValidator<ValidOptimizationRequest, List<Delivery>> {
    
    @Override
    public boolean isValid(List<Delivery> deliveries, ConstraintValidatorContext context) {
        if (deliveries == null || deliveries.isEmpty()) {
            return false;
        }
        
        return deliveries.stream().allMatch(delivery -> 
            delivery != null &&
            delivery.getTimeWindow() != null && 
            !delivery.getTimeWindow().isBlank() &&
            delivery.getLatitude() != null && 
            delivery.getLongitude() != null
        );
    }
}
