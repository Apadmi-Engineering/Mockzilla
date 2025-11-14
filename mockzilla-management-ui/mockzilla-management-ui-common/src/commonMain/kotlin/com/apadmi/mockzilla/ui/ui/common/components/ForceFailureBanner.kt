@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.CircleCheck
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.assets.Play
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.SolidButton
import com.apadmi.mockzilla.ui.ui.common.theme.partialFailure
import com.apadmi.mockzilla.ui.ui.common.theme.success
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val overlayAlpha = 0.1f

enum class ForceFailureBannerState {
    FullFailure,
    Normal,
    PartialFailure,
    ;
}

@Composable
internal fun ForceFailureBanner(
    modifier: Modifier = Modifier,
    state: ForceFailureBannerState,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val titleAndSubtitle = when (state) {
        ForceFailureBannerState.FullFailure -> strings.widgets.globalControls.forcedFailureBannerConfig
        ForceFailureBannerState.PartialFailure -> strings.widgets.globalControls.partialFailureBannerConfig
        ForceFailureBannerState.Normal -> strings.widgets.globalControls.normalBehaviourBannerConfig
    }
    val borderAndTextColor = when (state) {
        ForceFailureBannerState.FullFailure -> MaterialTheme.colorScheme.error
        ForceFailureBannerState.PartialFailure -> MaterialTheme.colorScheme.partialFailure.primary
        ForceFailureBannerState.Normal -> MaterialTheme.colorScheme.success.primary
    }
    val backgroundColorOverlay = when (state) {
        ForceFailureBannerState.FullFailure -> MaterialTheme.colorScheme.errorContainer.copy(
            alpha = overlayAlpha
        )

        ForceFailureBannerState.PartialFailure -> MaterialTheme.colorScheme.partialFailure.container.copy(
            alpha = overlayAlpha
        )

        ForceFailureBannerState.Normal -> borderAndTextColor.copy(alpha = overlayAlpha)
    }

    val bannerIcon = when (state) {
        ForceFailureBannerState.FullFailure,
        ForceFailureBannerState.PartialFailure -> Icons.LightningBolt
        ForceFailureBannerState.Normal -> Icons.CircleCheck
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColorOverlay.compositeOver(
                    MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = borderAndTextColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(top = 20.dp, bottom = 12.dp, start = 16.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = bannerIcon,
                contentDescription = null,
                tint = borderAndTextColor
            )
            Column {
                Text(
                    color = borderAndTextColor,
                    text = titleAndSubtitle.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    color = borderAndTextColor,
                    text = titleAndSubtitle.subtitle,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(2.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state != ForceFailureBannerState.FullFailure) {
                // Force Fail Button
                CustomOutlineButton(
                    label = strings.widgets.globalControls.failButtonLabel,
                    leadingIcon = Icons.LightningBolt,
                    contentPadding = PaddingValues(12.dp),
                    variant = OutlineButtonVariant.Error,
                    onClick = onForceFailureClicked
                )
            }

            if (state != ForceFailureBannerState.Normal) {
                // Restore Api Button
                SolidButton(
                    label = strings.widgets.globalControls.restoreButtonLabel,
                    leadingIcon = Icons.Play,
                    backgroundColor = Color(0xFF_00_A6_3E),
                    contentColor = Color.White,
                    contentPadding = PaddingValues(12.dp),
                    onClick = onRestoreApiClicked
                )
            }
        }
    }
}

@Preview
@Composable
private fun GlobalControlsWidgetBannersPreview() = PreviewSurface {
    GlobalFailureConfigBannerPreview()
}

@Preview
@Composable
private fun GlobalControlsWidgetBannersDarkPreview() = PreviewSurface(darkTheme = true) {
    GlobalFailureConfigBannerPreview()
}

@Composable
private fun GlobalFailureConfigBannerPreview() {
    Column(
        modifier = Modifier.padding(8.dp).background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ForceFailureBannerState.entries.forEach {
            ForceFailureBanner(state = it, onRestoreApiClicked = {}, onForceFailureClicked = {})
        }
    }
}
