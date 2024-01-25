package ru.mfp.common.config.security.aop.impl

import org.springframework.stereotype.Component
import ru.mfp.common.config.security.aop.AnnotationProcessor
import ru.mfp.common.config.security.aop.SolvencyVerified
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.common.model.UserRole
import kotlin.reflect.KClass

@Component
class EmailVerifiedOrHigherProcessor : AnnotationProcessor {

    override fun getAnnotationClass(): KClass<out Annotation> = SolvencyVerified::class

    override fun process(authentication: JwtAuthentication, annotation: Annotation) {
        if (UserRole.NEW != authentication.role) {
            throwException()
        }
    }
}