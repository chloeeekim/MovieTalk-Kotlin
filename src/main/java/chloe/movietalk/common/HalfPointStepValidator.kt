package chloe.movietalk.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HalfPointStepValidator implements ConstraintValidator<HalfPointStep, Double> {
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;

        return Math.abs(value * 10 % 5) < 0.0001;
    }
}
