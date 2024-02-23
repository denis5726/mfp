package ru.mfp.common.config.security.aop.impl

import kotlin.reflect.KClass
import org.springframework.stereotype.Component
import ru.mfp.common.config.security.aop.AnnotationProcessor
import ru.mfp.common.config.security.aop.EmailVerified
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.common.model.UserRole

@Component
class NewProcessor : AnnotationProcessor {

    override fun getAnnotationClass(): KClass<out Annotation> = EmailVerified::class

    override fun process(authentication: JwtAuthentication, annotation: Annotation) {
        if (UserRole.NEW != authentication.role) {
            throwException()
        }
    }
}