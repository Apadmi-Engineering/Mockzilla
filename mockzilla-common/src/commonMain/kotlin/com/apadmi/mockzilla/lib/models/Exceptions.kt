@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.models

/**
 * Thrown when attempting to start the Mockzilla server on a port that is occupied by another
 * process. To resolve, either terminate the process occupying the port or update your
 * `MockzillaConfig`.
 *
 * @property port The number of the port that was used to start the Mockzilla server.
 * @property cause
 */
public class PortConflictException(public val port: Int, override val cause: Throwable?) : RuntimeException() {
    override val message: String
        get() = "Attempted to start Mockzilla server on a port that is already occupied by another process ($port)."
}
