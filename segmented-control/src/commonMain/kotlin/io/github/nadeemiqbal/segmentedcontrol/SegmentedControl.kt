package io.github.nadeemiqbal.segmentedcontrol

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.jvm.JvmName
import kotlin.math.roundToInt

/**
 * An iOS-style segmented control: a horizontal row of mutually-exclusive options with an
 * animated selection indicator.
 *
 * This string-based overload is a convenience wrapper around the [SegmentItem]-based overload —
 * each entry becomes a text-only, enabled segment.
 *
 * ```
 * var selected by remember { mutableStateOf(0) }
 * SegmentedControl(
 *     items = listOf("Day", "Week", "Month"),
 *     selectedIndex = selected,
 *     onSelectionChange = { selected = it },
 * )
 * ```
 *
 * @param items the segment labels. Must not be empty.
 * @param selectedIndex the index of the currently selected segment. Out-of-range values are
 *   coerced into range.
 * @param onSelectionChange called with the new index when the user selects a different segment.
 * @param modifier the [Modifier] applied to the control. For [SegmentedControlWidth.Equal], pass
 *   a width modifier such as `Modifier.fillMaxWidth()`.
 * @param style the [SegmentedControlStyle] preset.
 * @param colors the [SegmentedControlColors] used to render the control.
 * @param shape the [Shape] of the track and thumb.
 * @param animationSpec the animation used to move the selection indicator.
 * @param width how segment widths are distributed — see [SegmentedControlWidth].
 * @param enabled when `false`, the whole control is non-interactive and drawn muted.
 */
@Composable
@JvmName("SegmentedControlLabels")
fun SegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: SegmentedControlStyle = SegmentedControlDefaults.Style,
    colors: SegmentedControlColors = SegmentedControlDefaults.colors(style),
    shape: Shape = SegmentedControlDefaults.shapeFor(style),
    animationSpec: AnimationSpec<Float> = SegmentedControlDefaults.AnimationSpec,
    width: SegmentedControlWidth = SegmentedControlDefaults.Width,
    enabled: Boolean = true,
) {
    val segmentItems = remember(items) { items.map { SegmentItem(label = it) } }
    SegmentedControl(
        items = segmentItems,
        selectedIndex = selectedIndex,
        onSelectionChange = onSelectionChange,
        modifier = modifier,
        style = style,
        colors = colors,
        shape = shape,
        animationSpec = animationSpec,
        width = width,
        enabled = enabled,
    )
}

/**
 * An iOS-style segmented control: a horizontal row of mutually-exclusive options with an
 * animated selection indicator.
 *
 * Supports text-only, icon-only and icon+text segments, three [SegmentedControlStyle] presets,
 * per-segment [SegmentItem.enabled] state, equal- or content-width layout, right-to-left
 * layouts, and Left/Right arrow-key navigation when the control holds focus (desktop & web).
 *
 * ```
 * var selected by remember { mutableStateOf(0) }
 * SegmentedControl(
 *     items = listOf(
 *         SegmentItem("List", Icons.Default.List),
 *         SegmentItem("Grid", Icons.Default.GridView),
 *     ),
 *     selectedIndex = selected,
 *     onSelectionChange = { selected = it },
 *     style = SegmentedControlStyle.iOS,
 * )
 * ```
 *
 * @param items the segments to display. Must not be empty.
 * @param selectedIndex the index of the currently selected segment. Out-of-range values are
 *   coerced into range.
 * @param onSelectionChange called with the new index when the user selects a different,
 *   enabled segment. Disabled segments never invoke this callback.
 * @param modifier the [Modifier] applied to the control. For [SegmentedControlWidth.Equal], pass
 *   a width modifier such as `Modifier.fillMaxWidth()`.
 * @param style the [SegmentedControlStyle] preset.
 * @param colors the [SegmentedControlColors] used to render the control.
 * @param shape the [Shape] of the track and thumb.
 * @param animationSpec the animation used to move the selection indicator.
 * @param width how segment widths are distributed — see [SegmentedControlWidth].
 * @param enabled when `false`, the whole control is non-interactive and drawn muted.
 * @throws IllegalArgumentException if [items] is empty.
 */
@Composable
fun SegmentedControl(
    items: List<SegmentItem>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: SegmentedControlStyle = SegmentedControlDefaults.Style,
    colors: SegmentedControlColors = SegmentedControlDefaults.colors(style),
    shape: Shape = SegmentedControlDefaults.shapeFor(style),
    animationSpec: AnimationSpec<Float> = SegmentedControlDefaults.AnimationSpec,
    width: SegmentedControlWidth = SegmentedControlDefaults.Width,
    enabled: Boolean = true,
) {
    requireValidSegmentCount(items.size)

    val styleSpec = remember(style, shape) { resolveStyleSpec(style, shape) }
    val density = LocalDensity.current
    val safeIndex = selectedIndex.coerceIn(0, items.lastIndex)

    // Per-segment geometry in pixels, in the control's coordinate space, filled in by
    // onGloballyPositioned. The thumb animates between these measured rectangles.
    val offsets = remember(items.size) { mutableStateListOf(*Array(items.size) { 0f }) }
    val widths = remember(items.size) { mutableStateListOf(*Array(items.size) { 0f }) }

    val thumbOffset = remember { Animatable(0f) }
    val thumbWidth = remember { Animatable(0f) }
    var measured by remember(items.size) { mutableStateOf(false) }

    LaunchedThumbEffect(
        safeIndex = safeIndex,
        offsets = offsets.toList(),
        widths = widths.toList(),
        thumbOffset = thumbOffset,
        thumbWidth = thumbWidth,
        animationSpec = animationSpec,
        measured = measured,
        onMeasured = { measured = true },
    )

    Box(
        modifier = modifier
            .height(SegmentedControlDefaults.Height)
            .clip(styleSpec.trackShape)
            .background(colors.trackColor, styleSpec.trackShape)
            .focusable(enabled)
            .onKeyEvent { event ->
                handleArrowKey(event, items, safeIndex, enabled, onSelectionChange)
            }
            .padding(styleSpec.trackPadding),
    ) {
        // Selection indicator ("thumb") — drawn first so the segment content sits on top.
        if (thumbWidth.value > 0f) {
            Box(
                Modifier
                    .offset { IntOffset(thumbOffset.value.roundToInt(), 0) }
                    .width(with(density) { thumbWidth.value.toDp() })
                    .fillMaxHeight()
                    .shadow(
                        elevation = if (enabled) styleSpec.thumbElevation else 0.dp,
                        shape = styleSpec.thumbShape,
                    )
                    .background(colors.thumbColor, styleSpec.thumbShape),
            )
        }

        Row(Modifier.fillMaxHeight()) {
            items.forEachIndexed { index, item ->
                val segmentEnabled = enabled && item.enabled
                val selected = index == safeIndex
                Segment(
                    item = item,
                    selected = selected,
                    enabled = segmentEnabled,
                    colors = colors,
                    width = width,
                    showDivider = styleSpec.showDividers &&
                        !selected &&
                        index < items.lastIndex &&
                        index + 1 != safeIndex,
                    onClick = { if (segmentEnabled) onSelectionChange(index) },
                    modifier = Modifier
                        .let { if (width == SegmentedControlWidth.Equal) it.weight(1f) else it }
                        .fillMaxHeight()
                        .onGloballyPositioned { coords ->
                            offsets[index] = coords.positionInParent().x
                            widths[index] = coords.size.width.toFloat()
                        }
                        .testTag("segment_$index"),
                )
            }
        }
    }
}

/** Drives the [thumbOffset]/[thumbWidth] animatables toward the currently selected segment. */
@Composable
private fun LaunchedThumbEffect(
    safeIndex: Int,
    offsets: List<Float>,
    widths: List<Float>,
    thumbOffset: Animatable<Float, *>,
    thumbWidth: Animatable<Float, *>,
    animationSpec: AnimationSpec<Float>,
    measured: Boolean,
    onMeasured: () -> Unit,
) {
    LaunchedEffect(safeIndex, offsets, widths) {
        val targetOffset = offsets.getOrElse(safeIndex) { 0f }
        val targetWidth = widths.getOrElse(safeIndex) { 0f }
        if (targetWidth <= 0f) return@LaunchedEffect
        if (!measured) {
            // First real measurement — snap into place instead of sliding in from zero.
            thumbOffset.snapTo(targetOffset)
            thumbWidth.snapTo(targetWidth)
            onMeasured()
        } else {
            launch { thumbOffset.animateTo(targetOffset, animationSpec) }
            launch { thumbWidth.animateTo(targetWidth, animationSpec) }
        }
    }
}

@Composable
private fun RowScope.Segment(
    item: SegmentItem,
    selected: Boolean,
    enabled: Boolean,
    colors: SegmentedControlColors,
    width: SegmentedControlWidth,
    showDivider: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetContentColor = when {
        !enabled -> colors.disabledContentColor
        selected -> colors.selectedContentColor
        else -> colors.unselectedContentColor
    }
    val contentColor by animateColorAsState(targetContentColor)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .semantics {
                this.selected = selected
                this.role = Role.Tab
                if (!enabled) disabled()
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item.icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = item.label,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp),
                )
            }
            item.label?.let { label ->
                Text(
                    text = label,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = if (width == SegmentedControlWidth.Equal) {
                        TextOverflow.Ellipsis
                    } else {
                        TextOverflow.Clip
                    },
                )
            }
        }
        if (showDivider) {
            Box(
                Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(0.5f)
                    .width(1.dp)
                    .background(colors.dividerColor),
            )
        }
    }
}

/**
 * Handles Left/Right arrow keys for keyboard navigation. Returns `true` when the key was
 * consumed (selection moved), `false` otherwise.
 */
private fun handleArrowKey(
    event: KeyEvent,
    items: List<SegmentItem>,
    current: Int,
    enabled: Boolean,
    onSelectionChange: (Int) -> Unit,
): Boolean {
    if (!enabled || event.type != KeyEventType.KeyDown) return false
    val direction = when (event.key) {
        Key.DirectionLeft -> -1
        Key.DirectionRight -> 1
        else -> return false
    }
    val next = nextEnabledIndex(items, current, direction)
    return if (next != current) {
        onSelectionChange(next)
        true
    } else {
        false
    }
}
