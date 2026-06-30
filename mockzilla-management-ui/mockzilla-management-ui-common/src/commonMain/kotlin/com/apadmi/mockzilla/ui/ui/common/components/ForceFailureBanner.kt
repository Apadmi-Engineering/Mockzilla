@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.CircleCheck
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning

internal enum class ForceFailureBannerState {
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
    val colorScheme = MaterialTheme.colorScheme
    val titleAndSubtitle = when (state) {
        ForceFailureBannerState.FullFailure -> strings.widgets.globalControls.forcedFailureBannerConfig
        ForceFailureBannerState.PartialFailure -> strings.widgets.globalControls.partialFailureBannerConfig
        ForceFailureBannerState.Normal -> strings.widgets.globalControls.normalBehaviourBannerConfig
    }
    val accentColor = when(state) {
        ForceFailureBannerState.FullFailure -> colorScheme.error
        ForceFailureBannerState.Normal -> colorScheme.success.primary
        ForceFailureBannerState.PartialFailure -> colorScheme.warning.primary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .drawBehind {
                val strokeWidth = 3.dp.toPx()
                drawLine(
                    color = accentColor,
                    start = Offset(strokeWidth / 2, 0f),
                    end = Offset(strokeWidth / 2, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .border(1.dp, when(state) {
                ForceFailureBannerState.FullFailure -> colorScheme.error
                ForceFailureBannerState.Normal -> colorScheme.success.primary
                ForceFailureBannerState.PartialFailure -> colorScheme.warning.primary
            }, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titleAndSubtitle.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Text(
                text = titleAndSubtitle.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }

        if (state == ForceFailureBannerState.Normal) {
            BaseButton(
                label = strings.widgets.globalControls.failButtonLabel,
                variant = ButtonVariant.Outline,
                contentColor = MaterialTheme.colorScheme.error,
                size = ButtonSize.Sm,
                leadingIcon = Icons.LightningBolt,
                onClick = onForceFailureClicked
            )
        } else {
            BaseButton(
                label = strings.widgets.globalControls.restoreButtonLabel,
                variant = ButtonVariant.Outline,
                size = ButtonSize.Sm,
                leadingIcon = Icons.Default.Check,
                contentColor = MaterialTheme.colorScheme.success.primary,
                onClick = onRestoreApiClicked
            )
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
