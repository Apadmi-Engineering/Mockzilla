@file:Suppress("FILE_NAME_MATCH_CLASS", "MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.EditCircle
import com.apadmi.mockzilla.ui.ui.common.assets.EditUnderscore
import com.apadmi.mockzilla.ui.ui.common.assets.ErrorCircle
import com.apadmi.mockzilla.ui.ui.common.assets.InfoCircle
import com.apadmi.mockzilla.ui.ui.common.assets.RedirectCircle
import com.apadmi.mockzilla.ui.ui.common.assets.SuccessCircle
import com.apadmi.mockzilla.ui.ui.common.theme.httpStatus_fallback
import com.apadmi.mockzilla.ui.ui.common.utils.color
import io.ktor.http.HttpStatusCode
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val intervalDp = 6
private const val strokeAlpha = 0.3f

internal enum class PresetCardVariant {
    Selectable, Selected
}

@Composable
private fun DashboardOverridePreset.description(
    strings: Strings.Widgets.EndpointDetails.Presets.TypeDescriptions = LocalStrings.current.widgets.endpointDetails.presets.typeDescriptions
) =
    when (type?.exampleStatusCode()?.value ?: response.statusCode?.value) {
        in 100..199 -> strings.informational
        in 200..299 -> strings.success
        in 300..399 -> strings.redirect
        in 400..499 -> strings.error
        in 500..599 -> strings.error
        else -> strings.other
    }

private fun DashboardOverridePreset.Type.exampleStatusCode() = when (this) {
    DashboardOverridePreset.Type.ClientError -> HttpStatusCode.BadRequest
    DashboardOverridePreset.Type.Informational -> HttpStatusCode.Continue
    DashboardOverridePreset.Type.Other -> null
    DashboardOverridePreset.Type.Redirect -> HttpStatusCode.PermanentRedirect
    DashboardOverridePreset.Type.ServerError -> HttpStatusCode.InternalServerError
    DashboardOverridePreset.Type.Success -> HttpStatusCode.OK
}

private fun DashboardOverridePreset.color() =
    (type?.exampleStatusCode() ?: response.statusCode)?.color()
        ?: httpStatus_fallback

private fun DashboardOverridePreset.icon() = if (isManagementUiDefinedCustomPreset) {
    Icons.EditCircle
} else {
    when ((type?.exampleStatusCode() ?: response.statusCode)?.value) {
        in 100..199 -> Icons.InfoCircle
        in 200..299 -> Icons.SuccessCircle
        in 300..399 -> Icons.RedirectCircle
        in 400..499 -> Icons.ErrorCircle
        in 500..599 -> Icons.ErrorCircle
        else -> Icons.EditCircle
    }
}

@Composable
internal fun PresetCard(
    variant: PresetCardVariant,
    preset: DashboardOverridePreset,
    onClicked: (DashboardOverridePreset) -> Unit,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) = Column(
    Modifier.fillMaxWidth()
        .clip(shape = RoundedCornerShape(12.dp))
        .clickable {
            onClicked(preset)
        }.border(
            width = 1.dp,
            color = when (variant) {
                PresetCardVariant.Selected -> MaterialTheme.colorScheme.onBackground
                PresetCardVariant.Selectable -> MaterialTheme.colorScheme.outline
            },
            shape = RoundedCornerShape(12.dp)
        ).background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp)
        )
        .padding(12.dp),
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = preset.icon(),
            contentDescription = null,
            tint = preset.color()
        )
        Spacer(Modifier.size(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = preset.name, style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.size(8.dp))
        Tag(
            label = when (variant) {
                PresetCardVariant.Selected -> strings.appliedLabel
                PresetCardVariant.Selectable -> preset.response.statusCode?.value?.toString()
                    ?: strings.statusCodeFallback
            },
            textColor = preset.color(),
            borderColor = preset.color()
        )
    }
    Spacer(Modifier.size(8.dp))
    if (!preset.description.isNullOrBlank()) {
        Text(
            text = preset.description ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Spacer(Modifier.size(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Tag(
            label = preset.description(),
            textColor = preset.color(),
            borderColor = preset.color(),
            backgroundColor = preset.color().copy(alpha = 0.2f)
        )
        when (variant) {
            PresetCardVariant.Selected -> Tag(
                prefix = {
                    Icon(
                        modifier = Modifier.padding(vertical = 4.dp),
                        imageVector = Icons.EditUnderscore,
                        contentDescription = null,
                    )
                },
                label = strings.editLabel,
                textColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.outline
            )
            PresetCardVariant.Selectable -> Tag(
                label = strings.applyLabel,
                textColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.outline,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }

    preset.response.body?.takeIf { it.isNotBlank() }
        ?.let {
            Spacer(Modifier.size(8.dp))
            ExpandableResponseBody(preset.response.body ?: "")
        }
}

@Composable
internal fun Tag(
    modifier: Modifier = Modifier,
    prefix: @Composable () -> Unit = {},
    label: String,
    textColor: Color,
    borderColor: Color,
    backgroundColor: Color = Color.Transparent,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
) = Row(
    modifier = modifier
        .border(
            width = 1.dp,
            color = borderColor,
            shape = RoundedCornerShape(8.dp)
        )
        .background(
            color = backgroundColor.compositeOver(
                background = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(8.dp)
        )
        .padding(contentPadding),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    prefix()
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = textColor
    )
}

@Composable
internal fun NoPresetCard(
    strings: Strings = LocalStrings.current
) {
    val presetStrings = strings.widgets.endpointDetails.presets
    val strokeColor = MaterialTheme.colorScheme.onSurface.copy(alpha = strokeAlpha)
    Column(
        Modifier.fillMaxWidth().drawBehind {
            val stroke = Stroke(
                width = 2f, pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(intervalDp.dp.toPx(), intervalDp.dp.toPx()), 0f
                )
            )
            drawRoundRect(
                color = strokeColor, cornerRadius = CornerRadius(12.dp.toPx()), style = stroke
            )
        }.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = presetStrings.noPresetTitle,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = presetStrings.noPresetBody,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ExpandableResponseBody(body: String) {
    var isExpanded by remember { mutableStateOf(false) }
    var canExpand by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable(enabled = canExpand || isExpanded) {
                isExpanded = !isExpanded
            },
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = body,
            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            onTextLayout = {
                canExpand = it.hasVisualOverflow
            }
        )
        if (canExpand || isExpanded) {
            Box(
                Modifier.align(Alignment.BottomEnd).padding(4.dp).background(
                    MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
                    shape = CircleShape
                )
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.UnfoldLess else Icons.Default.UnfoldMore,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun NoPresetCardPreview() = PreviewSurface {
    Box(Modifier.padding(12.dp)) {
        NoPresetCard()
    }
}

@Preview
@Composable
private fun PresetCardPreviews(useDark: Boolean = false) = PreviewSurface(darkTheme = useDark) {
    Column(
        modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val preset = DashboardOverridePreset(
            "Preset Name",
            "Preset subtitle",
            DashboardOverridePreset.Type.Informational,
            PartialMockzillaHttpResponse(
                body = """
                    {
                        "hello": "world",
                        "goodbye": "ice-cream",
                        "neither": "biscuits",
                    }
                """.trimIndent(),
                statusCode = HttpStatusCode.Continue,
            ),
        )
        PresetCard(
            PresetCardVariant.Selected,
            preset,
            onClicked = {},
        )
        PresetCard(
            PresetCardVariant.Selectable,
            preset.copy(
                type = DashboardOverridePreset.Type.Informational,
                response = preset.response.copy(statusCode = HttpStatusCode.Continue)
            ),
            onClicked = {},
        )
        PresetCard(
            PresetCardVariant.Selectable,
            preset.copy(
                type = DashboardOverridePreset.Type.Success,
                response = preset.response.copy(statusCode = HttpStatusCode.OK)
            ),
            onClicked = {},
        )

        PresetCard(
            PresetCardVariant.Selectable,
            preset.copy(
                type = DashboardOverridePreset.Type.Redirect,
                response = preset.response.copy(statusCode = HttpStatusCode.TemporaryRedirect)
            ),
            onClicked = {},
        )
        PresetCard(
            PresetCardVariant.Selectable,
            preset.copy(
                type = DashboardOverridePreset.Type.ServerError,
                response = preset.response.copy(statusCode = HttpStatusCode.InternalServerError)
            ),
            onClicked = {},
        )
        PresetCard(
            PresetCardVariant.Selectable,
            preset.copy(
                type = DashboardOverridePreset.Type.ServerError,
                response = preset.response.copy(statusCode = HttpStatusCode.OK)
            ),
            onClicked = {},
        )
        PresetCard(
            PresetCardVariant.Selectable,
            preset.copy(
                type = DashboardOverridePreset.Type.ServerError,
                response = preset.response.copy(statusCode = null),
                isManagementUiDefinedCustomPreset = true
            ),
            onClicked = {},
        )
        PresetCard(
            PresetCardVariant.Selectable,
            preset.copy(
                type = null,
                response = preset.response.copy(statusCode = null),
                isManagementUiDefinedCustomPreset = true
            ),
            onClicked = {},
        )
    }
}

@Preview
@Composable
private fun PresetCardDarkPreviews() = PresetCardPreviews(true)
