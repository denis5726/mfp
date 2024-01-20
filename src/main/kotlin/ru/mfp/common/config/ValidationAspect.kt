package ru.mfp.common.config

import jakarta.validation.Validator
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import ru.mfp.common.exception.DtoValidationException

@Aspect
@Component
class ValidationAspect(
    val validator: Validator
) {

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && execution(public * *(..))")
    fun validate(joinPoint: JoinPoint) {
        val violations = joinPoint.args.flatMap {
            validator.validate(it)
        }
        if (violations.isNotEmpty()) {
            throw DtoValidationException("Validation exception: " + violations.map { it.message })
        }
    }
}