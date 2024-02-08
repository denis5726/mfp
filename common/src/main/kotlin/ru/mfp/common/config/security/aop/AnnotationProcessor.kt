package ru.mfp.common.config.security.aop

import ru.mfp.common.exception.AuthorizationException
import ru.mfp.common.model.JwtAuthentication
import kotlin.reflect.KClass

interface AnnotationProcessor {

    fun getAnnotationClass(): KClass<out Annotation>

    fun process(authentication: JwtAuthentication, annotation: Annotation)

    fun throwException(): Nothing {
        throw AuthorizationException("Authorization failed")
    }
}