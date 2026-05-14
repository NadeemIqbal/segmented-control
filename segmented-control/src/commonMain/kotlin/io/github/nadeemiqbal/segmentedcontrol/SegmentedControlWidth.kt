package io.github.nadeemiqbal.segmentedcontrol

/**
 * Controls how segment widths are distributed in a [SegmentedControl].
 *
 * - [Equal] — every segment receives an equal share of the available width; long labels are
 *   truncated with an ellipsis. Pair this with a width modifier such as `Modifier.fillMaxWidth()`.
 * - [Content] — every segment is only as wide as its own content, and the control grows to fit.
 */
enum class SegmentedControlWidth {
    Equal,
    Content,
}
