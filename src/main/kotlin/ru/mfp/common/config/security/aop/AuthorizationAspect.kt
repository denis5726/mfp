package ru.mfp.common.config.security.aop

import jakarta.annotation.PostConstruct
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import ru.mfp.common.model.JwtAuthentication
import kotlin.reflect.KClass

@Component
@Aspect
class AuthorizationAspect(
    private val processors: List<AnnotationProcessor>
) {
    private val processorMap = mutableMapOf<KClass<out Annotation>, AnnotationProcessor>()

    @PostConstruct
    fun init() {
        processors.map {
            processorMap.put(it.getAnnotationClass(), it)
        }
    }

    @Before("execution(@ru.mfp.common.config.security.aop.* * *.*(..))")
    fun process(joinPoint: JoinPoint) {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthentication
        val signature = joinPoint.signature as MethodSignature
        signature.method.annotations.filter {
            processorMap.containsKey(it::class)
        }.forEach {
            processorMap[it::class]?.process(authentication, it)
        }
    }
}