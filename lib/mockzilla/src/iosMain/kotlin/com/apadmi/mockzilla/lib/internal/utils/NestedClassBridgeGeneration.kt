package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.MockzillaConfig

/**
 * For some infuriating reason, nested classes defined in dependent modules don't automatically have
 * headers generated unless they're being used within the module. This usage forces the generation
 * of the headers.
 */
fun noOpConfigBuilder(noop: MockzillaConfig.Builder) = Unit