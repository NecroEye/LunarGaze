package com.muratcangzm.lunargaze.common.utils

import javax.inject.Qualifier

/**
 * Qualifier for the Default CoroutineDispatcher
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Qualifier
annotation class DefaultDispatcher

/**
 * Qualifier for the IO CoroutineDispatcher
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Qualifier
annotation class IoDispatcher

/**
 * Qualifier for the Main CoroutineDispatcher
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Qualifier
annotation class MainDispatcher
