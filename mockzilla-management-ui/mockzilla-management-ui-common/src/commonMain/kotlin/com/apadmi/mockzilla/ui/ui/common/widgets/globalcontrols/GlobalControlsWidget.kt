package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.widgets.ResponseLatencyCard
import com.apadmi.mockzilla_management_ui_common.generated.resources.Res
import com.apadmi.mockzilla_management_ui_common.generated.resources.circle_check
import com.apadmi.mockzilla_management_ui_common.generated.resources.lightning_bolt
import com.apadmi.mockzilla_management_ui_common.generated.resources.play
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

// TODO: Migrate to using colours from ColorScheme
// TODO: Text styling

@Composable
fun GlobalControlsWidget(
    strings: Strings = LocalStrings.current
) = Column(
    modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = strings.widgets.globalControls.title)
            Text(text = strings.widgets.globalControls.subtitle)
        }
        CustomOutlineButton(
            label = strings.widgets.globalControls.resetAllLabel,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            onClick = {}  // TODO: Add button click
        )
    }

    Column(
        Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val isNormalApiBehaviour = false  // TODO: Use VM to determine this
        GlobalConfigBanner(
            strings = if (isNormalApiBehaviour) {
                strings.widgets.globalControls.normalBehaviourBannerConfig
            } else {
                strings.widgets.globalControls.forcedFailureBannerConfig
            },
            isNormalApiBehavior = isNormalApiBehaviour
        )
        ResponseLatencyCard(strings = strings, initialValue = 150)
    }
}

@Suppress("MAGIC_NUMBER")  // TODO: Remove
@Composable
private fun GlobalConfigBanner(
    strings: Strings.Widgets.GlobalControls.GlobalConfigBanner,
    isNormalApiBehavior: Boolean
) {
    val borderColor =
        if (isNormalApiBehavior) Color(0xFF_B9_F8_CF) else MaterialTheme.colorScheme.error
    val backgroundColor =
        if (isNormalApiBehavior) Color(0xFF_F0_FF_F5) else MaterialTheme.colorScheme.errorContainer.copy(
            alpha = 0.1f
        )
    val bannerIcon =
        if (isNormalApiBehavior) Res.drawable.circle_check else Res.drawable.lightning_bolt

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                // TODO: Add tint
                modifier = Modifier.size(20.dp),
                painter = painterResource(resource = bannerIcon),
                contentDescription = null
            )
            Column {
                Text(text = strings.title)
                Text(text = strings.subtitle)
            }
        }

        if (isNormalApiBehavior) {
            CustomOutlineButton(
                modifier = Modifier.align(Alignment.End),
                label = strings.buttonLabel,
                leadingIcon = painterResource(resource = Res.drawable.lightning_bolt),
                contentPadding = PaddingValues(12.dp),
                onClick = {}  // TODO: Add button click
            )
        } else {
            BaseButton(
                modifier = Modifier.align(Alignment.End),
                label = strings.buttonLabel,
                leadingIcon = painterResource(resource = Res.drawable.play),
                backgroundColor = Color(0xFF_00_A6_3E),
                contentColor = Color(0xFF_FF_FF_FF),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                onClick = {}  // TODO: Add button click
            )
        }
    }
}

@Preview
@Composable
private fun GlobalControlsWidgetPreview() = PreviewSurface {
    GlobalControlsWidget()
}
