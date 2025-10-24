@file:Suppress("KDOC_EXTRA_PROPERTY", "KDOC_NO_CONSTRUCTOR_PROPERTY")

package com.apadmi.mockzilla.demo.engine

import kotlinx.serialization.Serializable

@Serializable
data class AnimalDto(
    val animal: AnimalTypeDto,
    val name: String,
    val age: Int,
    val biography: String,
    val owner: String,
) {
    @Serializable
    enum class AnimalTypeDto {
        Cow,
        Pig,
        Sheep,
        ;
    }
}

@Serializable
data class GetAnimalRequestDto(val owner: String = "")
