package io.github.nadeemiqbal.segmentedcontrol

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Throws [IllegalArgumentException] when [count] is not a valid number of segments.
 *
 * Extracted from the composable so it can be unit-tested directly without composition.
 */
internal fun requireValidSegmentCount(count: Int) {
    require(count > 0) {
        "SegmentedControl requires at least one item; the items list was empty."
    }
}

/**
 * Returns the index of the nearest selectable (enabled) segment starting from [from] and moving
 * by [direction] (`-1` for left, `+1` for right). Returns [from] unchanged when there is no
 * enabled segment in that direction — used to implement keyboard navigation.
 */
internal fun nextEnabledIndex(items: List<SegmentItem>, from: Int, direction: Int): Int {
    var i = from + direction
    while (i in items.indices) {
        if (items[i].enabled) return i
        i += direction
    }
    return from
}

/** Resolved, non-color visual attributes for a [SegmentedControlStyle]. */
internal data class StyleSpec(
    val trackShape: Shape,
    val thumbShape: Shape,
    val thumbElevation: Dp,
    val showDividers: Boolean,
    val trackPadding: Dp,
)

/** Maps a [SegmentedControlStyle] (and the caller-supplied track [shape]) to a [StyleSpec]. */
internal fun resolveStyleSpec(style: SegmentedControlStyle, shape: Shape): StyleSpec = when (style) {
    SegmentedControlStyle.iOS -> StyleSpec(
        trackShape = shape,
        thumbShape = shape,
        thumbElevation = 2.dp,
        showDividers = true,
        trackPadding = 2.dp,
    )
    SegmentedControlStyle.Material3 -> StyleSpec(
        trackShape = shape,
        thumbShape = shape,
        thumbElevation = 0.dp,
        showDividers = false,
        trackPadding = 2.dp,
    )
    SegmentedControlStyle.Pill -> StyleSpec(
        trackShape = shape,
        thumbShape = shape,
        thumbElevation = 1.dp,
        showDividers = false,
        trackPadding = 3.dp,
    )
}
