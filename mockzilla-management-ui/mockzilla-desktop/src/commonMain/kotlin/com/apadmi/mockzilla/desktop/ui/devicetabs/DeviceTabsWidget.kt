package com.apadmi.mockzilla.desktop.ui.devicetabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel.State
import com.apadmi.mockzilla.desktop.ui.utils.desktopTertiaryPointerClick
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.success

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import kotlin.Float
import kotlin.math.roundToInt

private val horizontalOsxButtonPadding = 70.dp

@Composable
fun DeviceTabsWidget(
    modifier: Modifier,
) {
    val viewModel = getViewModel<DeviceTabsViewModel>()
    val state by viewModel.state.collectAsState()

    DeviceTabsWidgetContent(
        state = state,
        onSelect = viewModel::onChangeDevice,
        onAddNewDevice = viewModel::addNewDevice,
        onCloseTab = viewModel::removeDevice,
        modifier = modifier,
    )
}

@Composable
fun DeviceTabsWidgetContent(
    state: State,
    modifier: Modifier = Modifier,
    strings: Strings = LocalStrings.current,
    onSelect: (State.DeviceTabEntry) -> Unit,
    onAddNewDevice: () -> Unit,
    onCloseTab: (State.DeviceTabEntry) -> Unit,
) = Column {
    val colorScheme = MaterialTheme.colorScheme
    val scrollState = rememberScrollState()

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .background(colorScheme.background)
    ) {

        if (state.devices.isNotEmpty()) {
            HorizontalDivider(
                color = colorScheme.outline,
                thickness = 1.dp
            )
        }

        Box(
           modifier =
                Modifier
                    .padding(
                        start = if (hostOs == OS.MacOS) horizontalOsxButtonPadding else 0.dp,
                    )
                    .height(IntrinsicSize.Min)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.Bottom,
            ) {
                Box(Modifier.width(8.dp)) // Accounts for left curve of active tab background

                state.devices.forEach { device ->
                    DeviceTab(
                        device = device,
                        strings = strings,
                        onSelect = { onSelect(device) },
                        onClose = { onCloseTab(device) },
                        shape = TabShapeConfig()
                    )
                }


                if (state.devices.isNotEmpty()) {
                    IconButton(onClick = onAddNewDevice) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Filled.Add,
                            tint = colorScheme.onSurfaceVariant,
                            contentDescription = strings.widgets.deviceTabs.addDevice,
                        )
                    }
                }
            }

            FadeEdge(
                modifier = Modifier.align(Alignment.CenterStart),
                width = 36.dp,
                color = colorScheme.background,
                direction = FadeDirection.Left,
                visible = scrollState.value > 0,
            )
            FadeEdge(
                modifier = Modifier.align(Alignment.CenterEnd),
                width = 52.dp,
                color = colorScheme.background,
                direction = FadeDirection.Right,
                visible = scrollState.value < scrollState.maxValue,
            )
        }
    }

}


private enum class FadeDirection { Left, Right }

@Composable
private fun FadeEdge(
    modifier: Modifier = Modifier,
    width: Dp,
    color: Color,
    direction: FadeDirection,
    visible: Boolean,
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = fadeIn(),
    exit = fadeOut(),
) {
    Spacer(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .padding(bottom = 1.dp) // Avoid the border
            .background(
                brush = Brush.horizontalGradient(
                    colors = when (direction) {
                        FadeDirection.Left -> listOf(color, Color.Transparent)
                        FadeDirection.Right -> listOf(Color.Transparent, color)
                    }
                )
            )
    )
}

data class TabShapeConfig(
    val shoulderWidth: Dp = 12.dp,
    val shoulderDepth: Dp = 19.dp,
    val tail: Dp = 27.dp,
    val curl: Float = 0.15f,
)

fun DrawScope.drawTab(
    config: TabShapeConfig,
    background: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    strokeWidth: Dp = 1.dp,
) {
    val w = size.width
    val h = size.height
    val sw = config.shoulderWidth.toPx()
    val sd = config.shoulderDepth.toPx()
    val t = config.tail.toPx()

    val path = Path().apply {
        moveTo(-t, h)
        // Bottom left curve
        cubicTo(0f, h, 0f, h, 0f, h - sd)
        lineTo(0f, sw)

        // Top left curve
        cubicTo(0f, 0f, 0f, 0f, sw, 0f)
        lineTo(w - sw, 0f)

        // Top right curve
        cubicTo(w, 0f, w, 0f, w, sw)

        // Bottom right curve
        lineTo(w, h - sd)
        cubicTo(w, h, w, h, w + t, h)
    }

    if (borderColor != Color.Unspecified) {
        val paint = Paint().apply {
            this.color = borderColor
            this.style = PaintingStyle.Stroke
            this.strokeWidth = strokeWidth.toPx()
            this.strokeCap = StrokeCap.Round
            this.isAntiAlias = true
        }
        drawIntoCanvas {
            it.drawPath(
                path = path,
                paint = paint
            )
        }
    }

    if (background != Color.Unspecified) {
        drawPath(path.also { it.close() }, color = background)
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun DeviceTab(
    device: State.DeviceTabEntry,
    strings: Strings,
    onSelect: () -> Unit,
    onClose: () -> Unit,
    shape: TabShapeConfig,
) {
    val colorScheme = MaterialTheme.colorScheme
    val dotColor =
        if (device.isConnected) colorScheme.success.primary else colorScheme.onSurfaceFaint

    Box(
        modifier = Modifier
            .then(
                if (device.isActive) {
                    val background = colorScheme.surface
                    val outlineColor = colorScheme.outline
                    Modifier.drawBehind {
                        drawTab(
                            config = shape,
                            background = background,
                            borderColor = colorScheme.outline,
                        )
                    }
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onSelect)
            .desktopTertiaryPointerClick(onClick = onClose)
            // Slightly odd padding values needed here since the border curve throws things off
            .padding(start = 12.dp, end = 10.dp, top = 8.dp, bottom = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = 0.2f)),
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = device.appName,
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 11.sp),
                    color = if (device.isActive) colorScheme.onSurface else colorScheme.onSurfaceVariant,
                )
                Text(
                    text = if (device.isConnected) {
                        device.deviceName
                    } else {
                        strings.widgets.deviceTabs.disconnected
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = LocalMonoFontFamily.current
                    ),
                    color = colorScheme.onSurfaceVariant,
                )
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = strings.widgets.deviceTabs.closeButtonDescription,
                    tint = colorScheme.onSurfaceFaint,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun DeviceTabsWidgetPreviewLight() = PreviewSurface {
    DeviceTabsWidgetContent(
        state = State(
            devices = listOf(
                State.DeviceTabEntry(
                    appName = "Runner",
                    deviceName = "iPhone",
                    isActive = true,
                    isConnected = true,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
                State.DeviceTabEntry(
                    appName = "Runner",
                    deviceName = "Pixel 8 Pro",
                    isActive = false,
                    isConnected = false,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
            ),
        ),
        onSelect = {},
        onAddNewDevice = {},
        onCloseTab = {},
    )
}

@Preview
@Composable
private fun DeviceTabsWidgetPreviewDark() = PreviewSurface(darkTheme = true) {
    DeviceTabsWidgetContent(
        state = State(
            devices = listOf(
                State.DeviceTabEntry(
                    appName = "Runner",
                    deviceName = "iPhone",
                    isActive = true,
                    isConnected = true,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
                State.DeviceTabEntry(
                    appName = "Runner",
                    deviceName = "iPhone",
                    isActive = false,
                    isConnected = false,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
            ),
        ),
        onSelect = {},
        onAddNewDevice = {},
        onCloseTab = {},
    )
}
