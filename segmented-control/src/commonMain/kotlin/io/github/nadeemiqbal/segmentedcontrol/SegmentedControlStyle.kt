package io.github.nadeemiqbal.segmentedcontrol

/**
 * Visual style presets for [SegmentedControl].
 *
 * - [iOS] — a UIKit-style control: a tinted track, a white elevated thumb and hairline
 *   dividers between unselected segments. The platform-native look on iOS.
 * - [Material3] — a Material 3 flavoured control whose thumb is tinted with the app's
 *   primary color; no dividers, flat thumb.
 * - [Pill] — a fully-rounded "pill" track and thumb.
 */
@Suppress("EnumEntryName")
enum class SegmentedControlStyle {
    iOS,
    Material3,
    Pill,
}
