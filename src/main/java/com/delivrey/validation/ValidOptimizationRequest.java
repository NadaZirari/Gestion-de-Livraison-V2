package com.delivrey.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OptimizationRequestValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOptimizationRequest {
    String message() default "Invalid optimization request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
