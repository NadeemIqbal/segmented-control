package io.github.nadeemiqbal.segmentedcontrol.sample

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.nadeemiqbal.segmentedcontrol.SegmentItem
import io.github.nadeemiqbal.segmentedcontrol.SegmentedControl
import io.github.nadeemiqbal.segmentedcontrol.SegmentedControlDefaults
import io.github.nadeemiqbal.segmentedcontrol.SegmentedControlStyle
import io.github.nadeemiqbal.segmentedcontrol.SegmentedControlWidth

private enum class ContentMode { TextOnly, IconOnly, IconAndText }

private val labels = listOf("Day", "Week", "Month")
private val icons: List<ImageVector> = listOf(Icons.Default.Today, Icons.Default.DateRange, Icons.Default.CalendarMonth)

/** Shared sample UI — one scrollable screen exercising every [SegmentedControl] feature. */
@Composable
fun SampleApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            SampleContent()
        }
    }
}

@Composable
private fun SampleContent() {
    var selected by remember { mutableStateOf(0) }
    var contentMode by remember { mutableStateOf(ContentMode.TextOnly) }
    var widthMode by remember { mutableStateOf(SegmentedControlWidth.Equal) }
    var disableMiddle by remember { mutableStateOf(false) }
    var controlEnabled by remember { mutableStateOf(true) }

    val items: List<SegmentItem> = remember(contentMode, disableMiddle) {
        List(labels.size) { index ->
            val enabled = !disableMiddle || index != 1
            when (contentMode) {
                ContentMode.TextOnly -> SegmentItem(label = labels[index], enabled = enabled)
                ContentMode.IconOnly -> SegmentItem(icon = icons[index], enabled = enabled)
                ContentMode.IconAndText -> SegmentItem(label = labels[index], icon = icons[index], enabled = enabled)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text("SegmentedControl", style = MaterialTheme.typography.headlineMedium)
        Text(
            "An iOS-style segmented control for Compose Multiplatform.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SectionCard("Controls") {
            LabeledControl("Content") {
                SegmentedControl(
                    items = listOf("Text", "Icon", "Both"),
                    selectedIndex = contentMode.ordinal,
                    onSelectionChange = { contentMode = ContentMode.entries[it] },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            LabeledControl("Width") {
                SegmentedControl(
                    items = listOf("Equal", "Content"),
                    selectedIndex = widthMode.ordinal,
                    onSelectionChange = { widthMode = SegmentedControlWidth.entries[it] },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            ToggleRow("Disable middle segment", disableMiddle) { disableMiddle = it }
            ToggleRow("Control enabled", controlEnabled) { controlEnabled = it }
        }

        for (style in SegmentedControlStyle.entries) {
            SectionCard("Style: $style") {
                SegmentedControl(
                    items = items,
                    selectedIndex = selected,
                    onSelectionChange = { selected = it },
                    style = style,
                    width = widthMode,
                    enabled = controlEnabled,
                    modifier = if (widthMode == SegmentedControlWidth.Equal) Modifier.fillMaxWidth() else Modifier,
                )
            }
        }

        SectionCard("Customized (colors + shape + animation)") {
            SegmentedControl(
                items = items,
                selectedIndex = selected,
                onSelectionChange = { selected = it },
                style = SegmentedControlStyle.Pill,
                width = widthMode,
                enabled = controlEnabled,
                colors = SegmentedControlDefaults.colors(SegmentedControlStyle.Pill).copy(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                    selectedContentColor = MaterialTheme.colorScheme.onTertiary,
                ),
                shape = RoundedCornerShape(6.dp),
                animationSpec = tween(durationMillis = 450),
                modifier = if (widthMode == SegmentedControlWidth.Equal) Modifier.fillMaxWidth() else Modifier,
            )
        }

        Text(
            "Selected: ${labels[selected]} (index $selected)",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun LabeledControl(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
