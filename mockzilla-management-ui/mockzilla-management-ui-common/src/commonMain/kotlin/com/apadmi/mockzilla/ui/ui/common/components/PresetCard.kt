@file:Suppress("FILE_NAME_MATCH_CLASS", "MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.EditUnderscore
import com.apadmi.mockzilla.ui.ui.common.components.editor.EditorMode
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.theme.StateColors
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning
import com.apadmi.mockzilla.ui.ui.common.utils.formatting.BodyVisualTransformation
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.RowDensity
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.minifyJson
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.prettyPrintJson
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.typeFormat

import io.ktor.http.HttpStatusCode

private const val intervalDp = 6
private const val strokeAlpha = 0.3f

internal enum class PresetCardVariant {
    Selectable, Selected
}

@Composable
internal fun DashboardOverridePreset.statusColors(): StateColors {
    val colorScheme = MaterialTheme.colorScheme
    val effectiveType = type ?: response.statusCode?.let {
        when (it.value) {
            in 200..299 -> DashboardOverridePreset.Type.Success
            in 400..499 -> DashboardOverridePreset.Type.ClientError
            in 500..599 -> DashboardOverridePreset.Type.ServerError
            in 300..399 -> DashboardOverridePreset.Type.Redirect
            else -> DashboardOverridePreset.Type.Other
        }
    }

    return when (effectiveType) {
        DashboardOverridePreset.Type.Success -> colorScheme.success
        DashboardOverridePreset.Type.ServerError -> StateColors(colorScheme.error, colorScheme.errorContainer)
        DashboardOverridePreset.Type.ClientError -> colorScheme.warning
        DashboardOverridePreset.Type.Redirect -> colorScheme.warning
        else -> StateColors(colorScheme.onSurfaceVariant, colorScheme.surfaceVariant)
    }
}

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    prefix: @Composable () -> Unit = {},
    label: String,
    textColor: Color,
    borderColor: Color,
    backgroundColor: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
) = Row(
    modifier = modifier
        .border(
            width = 1.dp,
            color = borderColor,
            shape = shape
        )
        .background(
            color = backgroundColor.compositeOver(
                background = MaterialTheme.colorScheme.surface
            ),
            shape = shape
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
@Suppress("LONG_NUMERICAL_VALUES_SEPARATED")
internal fun PresetCard(
    variant: PresetCardVariant,
    preset: DashboardOverridePreset,
    onClicked: (DashboardOverridePreset) -> Unit,
    onEdit: () -> Unit = {},
    layoutMode: RowDensity = RowDensity.Compact,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) {
    val isDark = LocalForceDarkMode.current
    val isCompact = layoutMode == RowDensity.Compact
    val isSelected = variant == PresetCardVariant.Selected
    val shape = if (isDark) RoundedCornerShape(0.dp) else RoundedCornerShape(8.dp)
    val statusColors = preset.statusColors()
    val colorScheme = MaterialTheme.colorScheme
    val indicatorColor = if (isSelected) colorScheme.primary else statusColors.primary
    val iconTint = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant
    val titleColor = if (isSelected) colorScheme.primary else colorScheme.onSurface

    val hasExpandableContent = !preset.response.body.isNullOrBlank()
    var expanded by rememberSaveable { mutableStateOf(true) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 0f else -90f,
        animationSpec = tween(200),
        label = "chevron",
    )
    val chevronTint = if (hasExpandableContent) iconTint else colorScheme.onSurface.copy(alpha = 0.3f)

    Column(
        Modifier.fillMaxWidth()
            .clip(shape = shape)
            .focusProperties { canFocus = false }
            .background(if (isSelected) colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
            .drawBehind {
                val indicatorWidth = 2.dp.toPx()
                val padding = 4.dp.toPx()
                drawRoundRect(
                    cornerRadius = CornerRadius(indicatorWidth, indicatorWidth),
                    color = indicatorColor,
                    topLeft = Offset(padding / 2, padding.dp.toPx()),
                    size = Size(indicatorWidth, size.height - padding * 2)
                )
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = hasExpandableContent) { expanded = !expanded }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(16.dp).rotate(chevronRotation),
                tint = chevronTint,
            )
            Spacer(Modifier.size(8.dp))

            Text(
                modifier = Modifier.weight(1f),
                text = preset.name,
                style = MaterialTheme.typography.bodyMedium,
                color = titleColor,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            )
            Spacer(Modifier.size(8.dp))

            Tag(
                label = preset.response.statusCode?.value?.toString() ?: strings.statusCodeFallback,
                textColor = statusColors.primary,
                borderColor = statusColors.primary,
                backgroundColor = if (isDark) Color.Transparent else statusColors.container,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            )
            Spacer(Modifier.size(8.dp))

            when (variant) {
                PresetCardVariant.Selected -> Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onEdit() }
                    ) {
                        Tag(
                            modifier = Modifier.fillMaxHeight(),
                            prefix = {
                                Icon(
                                    imageVector = Icons.EditUnderscore,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = colorScheme.onSurface,
                                )
                            },
                            label = strings.editLabel,
                            textColor = colorScheme.onSurface,
                            borderColor = colorScheme.outline,
                            backgroundColor = colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        )
                    }

                    Tag(
                        modifier = Modifier.fillMaxHeight(),
                        prefix = {
                            Icon(
                                modifier = Modifier.size(14.dp),
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = colorScheme.onPrimary,
                            )
                        },
                        label = strings.appliedLabel,
                        textColor = colorScheme.onPrimary,
                        borderColor = Color.Transparent,
                        backgroundColor = colorScheme.primary,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    )
                }

                PresetCardVariant.Selectable -> Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onClicked(preset) }
                ) {
                    Tag(
                        label = strings.applyLabel,
                        textColor = colorScheme.onSurface,
                        borderColor = colorScheme.outline,
                        backgroundColor = colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    )
                }
            }
        }

        if (!preset.response.body.isNullOrBlank()) {
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    if (!preset.description.isNullOrBlank()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            text = preset.description ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceMuted,
                        )
                    }
                    Spacer(Modifier.size(4.dp))
                    ExpandableResponseBody(preset.response, isCompact)
                }
            }
        }
    }
}
@Composable
internal fun NoPresetCard(
    isCentered: Boolean = true,
    strings: Strings = LocalStrings.current
) {
    val presetStrings = strings.widgets.endpointDetails.presets
    val strokeColor = MaterialTheme.colorScheme.onSurface.copy(alpha = strokeAlpha)
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .drawBehind {
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
        verticalArrangement = if (isCentered) Arrangement.Center else Arrangement.Top
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
private fun ExpandableResponseBody(
    response: PartialMockzillaHttpResponse,
    isCompact: Boolean = false,
) = Box(
    modifier = Modifier
        .padding(horizontal = 12.dp)
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(8.dp)
        )
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = RoundedCornerShape(8.dp)
        )
        .clip(shape = RoundedCornerShape(8.dp))
) {
    val mode = response.typeFormat
    val body = when {
        BodyVisualTransformation.isBodyTooLarge(response.body) -> response.body?.take(1000) + "…"
        mode == EditorMode.Json -> if (isCompact) {
            response.body?.minifyJson()
        } else {
            response.body?.prettyPrintJson()
        }
        else -> response.body
    }
    Text(
        modifier = Modifier.padding(8.dp),
        text = (BodyVisualTransformation
            .buildEditorOutputTransformation(mode)
            ?.highlight(body ?: "")
            ?: AnnotatedString(body ?: "")),
        maxLines = if (isCompact) 4 else 16,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
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
