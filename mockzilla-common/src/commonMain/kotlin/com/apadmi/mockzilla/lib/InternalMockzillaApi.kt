package com.apadmi.mockzilla.lib

/**
 * API marked with this annotation is internal to Mockzilla and is not intended to be used outside
 * the library. It could be modified or removed without any notice. Please do not use it.
 *
 * Library modules opt in at the module level via `freeCompilerArgs` in their `build.gradle.kts`.
 * See `CONTRIBUTING.md` for guidance on when and how to apply this annotation.
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
    AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class InternalMockzillaApi
