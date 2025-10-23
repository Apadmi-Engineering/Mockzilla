@file:Suppress("MAGIC_NUMBER", "LONG_LINE")

package com.apadmi.mockzilla.demo.engine.mock

import android.content.Context
import com.apadmi.mockzilla.demo.engine.AnimalDto
import com.apadmi.mockzilla.demo.engine.GetAnimalRequestDto
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.startMockzilla
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

private val getMyPig = EndpointConfiguration
    .Builder("Pig")
    .setPatternMatcher { uri.endsWith("pig") }
    .configureDashboardOverrides {
        addPreset(
            name = "George",
            response = MockzillaHttpResponse(
                body = Json.encodeToString(
                    AnimalDto(
                        animal = AnimalDto.AnimalTypeDto.Pig,
                        name = "George",
                        age = 2,
                        biography = "George Pig is a fictional character in the hugely popular British animated television series, Peppa Pig. He is the younger brother of the show's star, Peppa Pig.",
                        owner = "Daddy Pig"
                    )
                )
            )
        )

        addPreset(
            name = "Pig Failure",
            response = MockzillaHttpResponse(statusCode = HttpStatusCode.NotFound)
        )

        addPreset(
            name = "Pig Malformed Response",
            response = MockzillaHttpResponse(body = "Pig Malformed Response")
        )
    }
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                AnimalDto(
                    animal = AnimalDto.AnimalTypeDto.Pig,
                    name = "Peppa",
                    age = 4,
                    biography = "Peppa Pig is the titular protagonist of the globally popular preschool animated series, Peppa Pig. She is a cheerful, energetic, and sometimes slightly bossy little pig.",
                    owner = Json.decodeFromString<GetAnimalRequestDto>(
                        bodyAsString()
                    ).owner
                )
            )
        )
    }

private val getMySheep = EndpointConfiguration
    .Builder("Sheep")
    .setPatternMatcher { uri.endsWith("sheep") }
    .configureDashboardOverrides {
        addPreset(
            name = "",
            response = MockzillaHttpResponse(
                body = Json.encodeToString(
                    AnimalDto(
                        animal = AnimalDto.AnimalTypeDto.Sheep,
                        name = "Dolly",
                        age = 6,
                        biography = "Dolly the Sheep was a female Finn-Dorset sheep who achieved global celebrity status as the first mammal to be successfully cloned from an adult somatic (body) cell. Her birth was a monumental scientific breakthrough that instantly redefined the possibilities of genetic engineering and ignited a fierce international debate on the ethics of cloning.",
                        owner = "Science"
                    )
                )
            )
        )

        addPreset(
            name = "Sheep Failure",
            response = MockzillaHttpResponse(statusCode = HttpStatusCode.BadRequest)
        )

        addPreset(
            name = "Sheep Malformed Response",
            response = MockzillaHttpResponse(body = "Sheep Malformed Response")
        )
    }
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                AnimalDto(
                    animal = AnimalDto.AnimalTypeDto.Sheep,
                    name = "Shaun",
                    age = 30,
                    biography = "Shaun the Sheep is the mischievous, intelligent, and resourceful protagonist of the stop-motion animated series of the same name.",
                    owner = Json.decodeFromString<GetAnimalRequestDto>(
                        bodyAsString()
                    ).owner
                )
            )
        )
    }

private val getMyCow = EndpointConfiguration
    .Builder("Cow")
    .setPatternMatcher { uri.endsWith("cow") }
    .configureDashboardOverrides {
        addPreset(
            name = "Angry Bessie",
            response = MockzillaHttpResponse(
                body = Json.encodeToString(
                    AnimalDto(
                        animal = AnimalDto.AnimalTypeDto.Cow,
                        name = "Big Bertha",
                        age = 82,
                        biography = "Big Bertha was a famous Droimeann cow from Ireland who achieved international notoriety and held two Guinness World Records for her extraordinary longevity and fertility. She was a national celebrity who spent her long life raising money for charity.",
                        owner = "The owner of Big Bertha is currently unknown.",
                    )
                )
            )
        )

        addPreset(
            name = "Cow Failure",
            response = MockzillaHttpResponse(statusCode = HttpStatusCode.NotFound)
        )

        addPreset(
            name = "Cow Malformed Response",
            response = MockzillaHttpResponse(body = "Cow Malformed Response")
        )
    }
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                AnimalDto(
                    animal = AnimalDto.AnimalTypeDto.Cow,
                    name = "Bessie",
                    age = 20,
                    biography = "Bessie the Cow comes in many forms and there are not enough words for her to be defined here.",
                    owner = Json.decodeFromString<GetAnimalRequestDto>(
                        bodyAsString()
                    ).owner
                )
            )
        )
    }

fun startMockServer(context: Context, isRelease: Boolean) = startMockzilla(
    config = MockzillaConfig.Builder()
        .addEndpoint(getMyCow)
        .addEndpoint(getMySheep)
        .addEndpoint(getMyPig)
        .setLogLevel(MockzillaConfig.LogLevel.Verbose)
        .setIsReleaseModeEnabled(isRelease)
        .build(),
    context = context
)
