package com.apadmi.mockzilla.mock

import kotlinx.serialization.Serializable

@Serializable
data class CowDto(
    val name: String,
    val age: Int,
    val likesGrass: Boolean,
    val hasHorns: Boolean,
    val mooSample: String,
    val someValueFromRequest: String,
) {
    companion object {
        val empty = CowDto("", 0, false, false, "", "")
    }
}

@Serializable
data class GetCowRequestDto(val aValueInTheRequest: String)