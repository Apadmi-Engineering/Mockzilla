package com.apadmi.mockzilla.lib

/**
 * Applied as `@file:NoKDoc` to opt a whole file out of diktat's KDoc-completeness rules.
 *
 * For use in files where KDocs offer little value such as ViewModels.
 */
@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
@InternalMockzillaApi
@Suppress("CLASS_NAME_INCORRECT")
public annotation class NoKDoc
