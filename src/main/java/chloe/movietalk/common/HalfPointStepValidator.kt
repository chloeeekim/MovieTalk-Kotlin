package chloe.movietalk.common

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.math.abs

class HalfPointStepValidator : ConstraintValidator<HalfPointStep?, Double?> {
    override fun isValid(value: Double?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true

        return abs(value * 10 % 5) < 0.0001
    }
}
