package chloe.movietalk.common

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [HalfPointStepValidator::class])
@MustBeDocumented
annotation class HalfPointStep(
    val message: String = "평점은 0.5점 단위여야 합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
