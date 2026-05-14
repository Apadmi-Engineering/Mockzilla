@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.CircleCheck
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.assets.Play
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.theme.MockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

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

private fun MockzillaTokens.bannerColors(state: ForceFailureBannerState) = when (state) {
    ForceFailureBannerState.FullFailure -> BannerColors(err, errSoft)
    ForceFailureBannerState.PartialFailure -> BannerColors(warn, warnSoft)
    ForceFailureBannerState.Normal -> BannerColors(ok, okSoft)
}

@Composable
internal fun ForceFailureBanner(
    modifier: Modifier = Modifier,
    state: ForceFailureBannerState,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val tokens = LocalMockzillaTokens.current
    val colors = tokens.bannerColors(state)
    val titleAndSubtitle = when (state) {
        ForceFailureBannerState.FullFailure -> strings.widgets.globalControls.forcedFailureBannerConfig
        ForceFailureBannerState.PartialFailure -> strings.widgets.globalControls.partialFailureBannerConfig
        ForceFailureBannerState.Normal -> strings.widgets.globalControls.normalBehaviourBannerConfig
    }
    val bannerIcon = when (state) {
        ForceFailureBannerState.FullFailure,
        ForceFailureBannerState.PartialFailure -> Icons.LightningBolt
        ForceFailureBannerState.Normal -> Icons.CircleCheck
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.soft, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = colors.accent, shape = RoundedCornerShape(10.dp))
            .padding(top = 16.dp, bottom = 10.dp, start = 14.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = bannerIcon,
                contentDescription = null,
                tint = colors.accent,
            )
            Column {
                Text(
                    color = colors.accent,
                    text = titleAndSubtitle.title,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    color = tokens.fg2,
                    text = titleAndSubtitle.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Spacer(Modifier.height(2.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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
        modifier = Modifier.padding(8.dp).background(LocalMockzillaTokens.current.bg0),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ForceFailureBannerState.entries.forEach {
            ForceFailureBanner(state = it, onRestoreApiClicked = {}, onForceFailureClicked = {})
        }
    }
}
