package com.apadmi.mockzilla.lib.service

@RequiresOptIn(message = "Mockzilla's web interface and everything surrounding it is legacy, awaiting rewriting. This feature is likely to change.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class MockzillaWeb
