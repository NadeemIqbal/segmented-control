package io.github.nadeemiqbal.segmentedcontrol

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Default values and factory functions used by [SegmentedControl]. */
object SegmentedControlDefaults {

    /** The default visual style. */
    val Style: SegmentedControlStyle = SegmentedControlStyle.iOS

    /** The default width distribution. */
    val Width: SegmentedControlWidth = SegmentedControlWidth.Equal

    /** The default corner radius used by the [SegmentedControlStyle.iOS] and [SegmentedControlStyle.Material3] tracks. */
    val CornerRadius: Dp = 8.dp

    /** The default height of the control. */
    val Height: Dp = 36.dp

    /** The default animation used to move the selection indicator between segments. */
    val AnimationSpec: AnimationSpec<Float>
        get() = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        )

    /** Returns the track [Shape] that best matches [style]. */
    fun shapeFor(style: SegmentedControlStyle): Shape = when (style) {
        SegmentedControlStyle.iOS -> RoundedCornerShape(CornerRadius)
        SegmentedControlStyle.Material3 -> RoundedCornerShape(CornerRadius)
        SegmentedControlStyle.Pill -> RoundedCornerShape(percent = 50)
    }

    /**
     * Creates a [SegmentedControlColors] tuned for [style] and derived from the current
     * [MaterialTheme]. Any parameter can be overridden; anything left at its default is
     * resolved from the active color scheme.
     */
    @Composable
    @ReadOnlyComposable
    fun colors(
        style: SegmentedControlStyle = Style,
        trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        thumbColor: Color = when (style) {
            SegmentedControlStyle.iOS -> MaterialTheme.colorScheme.surface
            SegmentedControlStyle.Material3 -> MaterialTheme.colorScheme.primary
            SegmentedControlStyle.Pill -> MaterialTheme.colorScheme.primary
        },
        selectedContentColor: Color = when (style) {
            SegmentedControlStyle.iOS -> MaterialTheme.colorScheme.onSurface
            SegmentedControlStyle.Material3 -> MaterialTheme.colorScheme.onPrimary
            SegmentedControlStyle.Pill -> MaterialTheme.colorScheme.onPrimary
        },
        unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        dividerColor: Color = when (style) {
            SegmentedControlStyle.iOS -> MaterialTheme.colorScheme.outlineVariant
            else -> Color.Transparent
        },
    ): SegmentedControlColors = SegmentedControlColors(
        trackColor = trackColor,
        thumbColor = thumbColor,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        disabledContentColor = disabledContentColor,
        dividerColor = dividerColor,
    )
}
