package ru.mfp.common.config.security.aop.impl

import kotlin.reflect.KClass
import org.springframework.stereotype.Component
import ru.mfp.common.config.security.aop.AnnotationProcessor
import ru.mfp.common.config.security.aop.ServiceOnly
import ru.mfp.common.model.JwtAuthentication

@Component
class ServiceOnlyProcessor : AnnotationProcessor {

    override fun getAnnotationClass(): KClass<out Annotation> = ServiceOnly::class

    override fun process(authentication: JwtAuthentication, annotation: Annotation) {
        if (JwtAuthentication.Mode.SERVICE != authentication.mode) {
            throwException()
        }
    }
}