package com.apadmi.mockzilla.lib.internal.utils

/**
 * API marked with this annotation is internal, and it is not intended to be used outside Mockzilla.
 * It could be modified or removed without any notice. Please do not use it.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is internal to Mockzilla and should not be used. It could be removed or changed without notice."
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.BINARY)
annotation class InternalMockzillaApi
