package com.apadmi.mockzilla.lib.service

@RequiresOptIn(message = "Web portal is now not supported. This is obsolete and will soon be removed")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Deprecated("Web api is no longer used, see ")
annotation class MockzillaWeb
