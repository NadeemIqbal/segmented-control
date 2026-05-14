package io.github.nadeemiqbal.segmentedcontrol

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * The color set used to render a [SegmentedControl].
 *
 * Obtain an instance from [SegmentedControlDefaults.colors], optionally overriding individual
 * colors, or construct one directly for full control.
 *
 * @property trackColor background color of the whole control.
 * @property thumbColor background color of the moving selection indicator.
 * @property selectedContentColor text/icon color of the selected segment.
 * @property unselectedContentColor text/icon color of unselected segments.
 * @property disabledContentColor text/icon color of disabled segments.
 * @property dividerColor color of the hairline dividers between unselected segments
 *   (drawn only by [SegmentedControlStyle.iOS]).
 */
@Immutable
class SegmentedControlColors(
    val trackColor: Color,
    val thumbColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color,
) {
    /** Returns a copy of this color set, overriding only the values that are passed. */
    fun copy(
        trackColor: Color = this.trackColor,
        thumbColor: Color = this.thumbColor,
        selectedContentColor: Color = this.selectedContentColor,
        unselectedContentColor: Color = this.unselectedContentColor,
        disabledContentColor: Color = this.disabledContentColor,
        dividerColor: Color = this.dividerColor,
    ): SegmentedControlColors = SegmentedControlColors(
        trackColor = trackColor,
        thumbColor = thumbColor,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        disabledContentColor = disabledContentColor,
        dividerColor = dividerColor,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SegmentedControlColors) return false
        return trackColor == other.trackColor &&
            thumbColor == other.thumbColor &&
            selectedContentColor == other.selectedContentColor &&
            unselectedContentColor == other.unselectedContentColor &&
            disabledContentColor == other.disabledContentColor &&
            dividerColor == other.dividerColor
    }

    override fun hashCode(): Int {
        var result = trackColor.hashCode()
        result = 31 * result + thumbColor.hashCode()
        result = 31 * result + selectedContentColor.hashCode()
        result = 31 * result + unselectedContentColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        result = 31 * result + dividerColor.hashCode()
        return result
    }

    override fun toString(): String =
        "SegmentedControlColors(trackColor=$trackColor, thumbColor=$thumbColor, " +
            "selectedContentColor=$selectedContentColor, unselectedContentColor=$unselectedContentColor, " +
            "disabledContentColor=$disabledContentColor, dividerColor=$dividerColor)"
}
