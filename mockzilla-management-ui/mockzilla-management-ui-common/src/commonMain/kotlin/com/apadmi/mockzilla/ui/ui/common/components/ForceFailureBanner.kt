@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.assets.Play
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning

import org.jetbrains.compose.ui.tooling.preview.Preview

private const val bannerCornerRadius = 8

enum class ForceFailureBannerState {
    FullFailure,
    Normal,
    PartialFailure,
    ;
}

/**
 * @property accent
 * @property soft
 */
private data class BannerColors(
    val accent: Color,
    val soft: Color,
)

@Composable
private fun ColorScheme.bannerColors(state: ForceFailureBannerState) = when (state) {
    ForceFailureBannerState.FullFailure -> BannerColors(error, surfaceContainerLow)
    ForceFailureBannerState.PartialFailure -> BannerColors(warning.primary, surfaceContainerLow)
    ForceFailureBannerState.Normal -> BannerColors(success.primary, surfaceContainerLow)
}

@Composable
internal fun ForceFailureBanner(
    modifier: Modifier = Modifier,
    state: ForceFailureBannerState,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val colorScheme = MaterialTheme.colorScheme
    val colors = colorScheme.bannerColors(state)
    val titleAndSubtitle = when (state) {
        ForceFailureBannerState.FullFailure -> strings.widgets.globalControls.forcedFailureBannerConfig
        ForceFailureBannerState.PartialFailure -> strings.widgets.globalControls.partialFailureBannerConfig
        ForceFailureBannerState.Normal -> strings.widgets.globalControls.normalBehaviourBannerConfig
    }

    val isDark = LocalForceDarkMode.current || isSystemInDarkTheme()
    val shape = RoundedCornerShape(bannerCornerRadius.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(shape)
            .background(color = if (isDark) colors.soft.copy(alpha = 0.5f) else colors.soft)
            .border(
                width = 1.dp,
                color = colors.accent.copy(alpha = 0.5f),
                shape = shape
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(colors.accent)
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    color = colors.accent,
                    text = titleAndSubtitle.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    color = colorScheme.onSurfaceMuted,
                    text = titleAndSubtitle.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state != ForceFailureBannerState.FullFailure) {
                    BaseButton(
                        label = strings.widgets.globalControls.failButtonLabel,
                        leadingIcon = Icons.LightningBolt,
                        variant = ButtonVariant.Danger,
                        size = ButtonSize.Sm,
                        onClick = onForceFailureClicked,
                    )
                }

                if (state != ForceFailureBannerState.Normal) {
                    BaseButton(
                        label = strings.widgets.globalControls.restoreButtonLabel,
                        leadingIcon = Icons.Play,
                        variant = ButtonVariant.Solid,
                        size = ButtonSize.Sm,
                        onClick = onRestoreApiClicked,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ForceFailureBannerPreview() = PreviewSurface {
    GlobalFailureConfigBannerPreview()
}

@Preview
@Composable
private fun ForceFailureBannerDarkPreview() = PreviewSurface(darkTheme = true) {
    GlobalFailureConfigBannerPreview()
}

@Composable
private fun GlobalFailureConfigBannerPreview() {
    Column(
        modifier = Modifier.padding(8.dp).background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ForceFailureBannerState.entries.forEach {
            ForceFailureBanner(state = it, onRestoreApiClicked = {}, onForceFailureClicked = {})
        }
    }
}
