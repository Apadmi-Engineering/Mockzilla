package screenshots

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface

import java.util.Locale

class TypographyTestPreview(
    private val showkaseBrowserTypography: ShowkaseBrowserTypography
) : TestPreview {
    override val type = TestType.Typography

    @Composable
    override fun Content() = PreviewSurface(color = MaterialTheme.colorScheme.surface) {
        Text(
            text = showkaseBrowserTypography.typographyName.replaceFirstChar {
                it.titlecase(Locale.UK)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = showkaseBrowserTypography.textStyle,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    override fun toString(): String =
        "${showkaseBrowserTypography.typographyGroup} - ${showkaseBrowserTypography.typographyName}"
}
