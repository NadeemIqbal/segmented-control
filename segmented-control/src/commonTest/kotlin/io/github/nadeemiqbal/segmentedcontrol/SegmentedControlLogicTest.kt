package io.github.nadeemiqbal.segmentedcontrol

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Pure-logic tests — no composition required. Run on every target including Android unit tests. */
class SegmentedControlLogicTest {

    @Test
    fun requireValidSegmentCount_throwsHelpfulMessageOnEmpty() {
        val error = assertFailsWith<IllegalArgumentException> {
            requireValidSegmentCount(0)
        }
        assertTrue(
            error.message?.contains("at least one item") == true,
            "message should explain the requirement, was: ${error.message}",
        )
    }

    @Test
    fun requireValidSegmentCount_passesForOneOrMore() {
        requireValidSegmentCount(1)
        requireValidSegmentCount(7)
    }

    @Test
    fun segmentItem_requiresLabelOrIcon() {
        assertFailsWith<IllegalArgumentException> { SegmentItem() }
    }

    @Test
    fun segmentItem_acceptsLabelOnlyAndIsEnabledByDefault() {
        val item = SegmentItem(label = "Day")
        assertEquals("Day", item.label)
        assertTrue(item.enabled)
    }

    @Test
    fun nextEnabledIndex_movesRightSkippingDisabled() {
        val items = listOf(
            SegmentItem("A"),
            SegmentItem("B", enabled = false),
            SegmentItem("C"),
        )
        assertEquals(2, nextEnabledIndex(items, from = 0, direction = 1))
    }

    @Test
    fun nextEnabledIndex_movesLeftSkippingDisabled() {
        val items = listOf(
            SegmentItem("A"),
            SegmentItem("B", enabled = false),
            SegmentItem("C"),
        )
        assertEquals(0, nextEnabledIndex(items, from = 2, direction = -1))
    }

    @Test
    fun nextEnabledIndex_staysWhenNoEnabledNeighbour() {
        val items = listOf(
            SegmentItem("A"),
            SegmentItem("B", enabled = false),
        )
        assertEquals(0, nextEnabledIndex(items, from = 0, direction = 1))
    }

    @Test
    fun nextEnabledIndex_staysAtEdges() {
        val items = listOf(SegmentItem("A"), SegmentItem("B"))
        assertEquals(0, nextEnabledIndex(items, from = 0, direction = -1))
        assertEquals(1, nextEnabledIndex(items, from = 1, direction = 1))
    }

    @Test
    fun resolveStyleSpec_onlyIosShowsDividers() {
        val iosShape = SegmentedControlDefaults.shapeFor(SegmentedControlStyle.iOS)
        assertTrue(resolveStyleSpec(SegmentedControlStyle.iOS, iosShape).showDividers)
        assertFalse(resolveStyleSpec(SegmentedControlStyle.Material3, iosShape).showDividers)
        assertFalse(resolveStyleSpec(SegmentedControlStyle.Pill, iosShape).showDividers)
    }

    @Test
    fun segmentedControlColors_copyOverridesOnlyGivenValues() {
        val base = SegmentedControlColors(
            trackColor = androidx.compose.ui.graphics.Color.Gray,
            thumbColor = androidx.compose.ui.graphics.Color.White,
            selectedContentColor = androidx.compose.ui.graphics.Color.Black,
            unselectedContentColor = androidx.compose.ui.graphics.Color.DarkGray,
            disabledContentColor = androidx.compose.ui.graphics.Color.LightGray,
            dividerColor = androidx.compose.ui.graphics.Color.Black,
        )
        val updated = base.copy(thumbColor = androidx.compose.ui.graphics.Color.Red)
        assertEquals(androidx.compose.ui.graphics.Color.Red, updated.thumbColor)
        assertEquals(base.trackColor, updated.trackColor)
        assertEquals(base, base.copy())
    }
}
