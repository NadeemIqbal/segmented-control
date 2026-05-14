package io.github.nadeemiqbal.segmentedcontrol

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A single segment in a [SegmentedControl].
 *
 * A segment must carry a [label], an [icon], or both — constructing one with neither throws
 * [IllegalArgumentException]. Set [enabled] to `false` to render the segment greyed-out and
 * non-interactive while it still occupies its slot.
 *
 * ```
 * SegmentItem("List", Icons.Default.List)          // icon + text
 * SegmentItem(icon = Icons.Default.GridView)        // icon only
 * SegmentItem("Month", enabled = false)             // disabled text segment
 * ```
 *
 * @property label optional text shown in the segment.
 * @property icon optional leading icon shown in the segment.
 * @property enabled whether the segment can be selected. Defaults to `true`.
 */
@Immutable
data class SegmentItem(
    val label: String? = null,
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
) {
    init {
        require(label != null || icon != null) {
            "SegmentItem requires a label, an icon, or both — both were null."
        }
    }
}
