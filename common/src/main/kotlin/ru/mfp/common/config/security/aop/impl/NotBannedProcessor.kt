package ru.mfp.common.config.security.aop.impl

import kotlin.reflect.KClass
import org.springframework.stereotype.Component
import ru.mfp.common.config.security.aop.AnnotationProcessor
import ru.mfp.common.config.security.aop.NotBanned
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.common.model.UserRole

@Component
class NotBannedProcessor : AnnotationProcessor {

    override fun getAnnotationClass(): KClass<out Annotation> = NotBanned::class

    override fun process(authentication: JwtAuthentication, annotation: Annotation) {
        if (UserRole.BANNED == authentication.role) {
            throwException()
        }
    }
}