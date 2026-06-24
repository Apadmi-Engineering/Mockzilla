package com.apadmi.mockzilla.desktop.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class DesktopLogWriter(
    private val minSeverity: Severity = Severity.Verbose,
    private val useColor: Boolean = true
) : LogWriter() {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

    override fun isLoggable(tag: String, severity: Severity): Boolean =
        severity >= minSeverity

    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
        val timestamp = LocalDateTime.now().format(timeFormatter)
        val levelChar = severity.name.first()  // V D I W E A
        val plain = "$timestamp $levelChar/Mockzilla/$tag: $message"

        val line = if (useColor) "${colorFor(severity)}$plain$RESET" else plain
        val stream = if (severity >= Severity.Warn) System.err else System.out
        stream.println(line)

        throwable?.let {
            val trace = it.stackTraceToString()
            stream.println(if (useColor) "${colorFor(severity)}$trace$RESET" else trace)
        }
    }

    private fun colorFor(severity: Severity): String = when (severity) {
        Severity.Verbose -> "\u001B[90m"  // grey
        Severity.Debug -> "\u001B[36m"  // cyan
        Severity.Info -> "\u001B[32m"  // green
        Severity.Warn -> "\u001B[33m"  // yellow
        Severity.Error -> "\u001B[31m"  // red
        Severity.Assert -> "\u001B[35m"  // magenta
    }

    companion object {
        private const val RESET = "\u001B[0m"
    }
}
