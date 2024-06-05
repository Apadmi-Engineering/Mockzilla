package com.apadmi.mockzilla.lib.config

object ZeroConfConfig {
    const val serviceType = "_mockzilla._tcp"

    // Limit defined here: https://datatracker.ietf.org/doc/html/rfc1035#section-2.3.1
    const val serviceNameByteLimit = 63
}
