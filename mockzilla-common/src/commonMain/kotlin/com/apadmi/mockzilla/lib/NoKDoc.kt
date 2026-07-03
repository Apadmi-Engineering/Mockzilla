package com.apadmi.mockzilla.lib

/**
 * Applied as `@file:NoKDoc` to opt a whole file out of diktat's KDoc-completeness rules.
 *
 * This module has no real external API surface (see the CONTRIBUTING.md exception for
 * `mockzilla-management-ui-common`), so requiring/auto-generating `@property`, `@param`
 * and `@return` stubs on internal ViewModel state, DTOs etc. only produces noise.
 */
@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
@InternalMockzillaApi
@Suppress("CLASS_NAME_INCORRECT")
public annotation class NoKDoc
