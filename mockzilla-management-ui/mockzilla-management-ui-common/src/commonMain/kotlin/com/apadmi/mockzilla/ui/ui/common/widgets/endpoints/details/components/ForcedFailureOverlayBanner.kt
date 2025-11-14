package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("COMPLEX_EXPRESSION")
@Composable
internal fun ForcedFailureOverlayBanner(
    strings: Strings.Widgets.EndpointDetails = LocalStrings.current.widgets.endpointDetails,
    borderShape: Shape = RoundedCornerShape(12.dp)
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            shape = borderShape
        )
        .pointerInput(Unit) {
            detectTapGestures { /* Absorb touches */ }
        }
        .semantics(mergeDescendants = true) { /* No-Op */ }
        .padding(horizontal = 12.dp)
        .zIndex(1f),
    contentAlignment = Alignment.Center
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f).compositeOver(
                    background = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.LightningBolt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Column {
            Text(
                text = strings.forcedApiFailureBannerTitle,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = strings.forcedApiFailureBannerSubtitle,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
private fun ForcedFailureOverlayBannerPreview() = PreviewSurface {
    ForcedFailureOverlayBanner()
}

@Preview
@Composable
private fun ForcedFailureOverlayBannerDarkPreview() = PreviewSurface(darkTheme = true) {
    ForcedFailureOverlayBanner()
}
