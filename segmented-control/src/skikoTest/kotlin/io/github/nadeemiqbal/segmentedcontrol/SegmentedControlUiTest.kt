package io.github.nadeemiqbal.segmentedcontrol

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/** Compose UI tests — Skiko-backed, run on Desktop and iOS test targets. */
@OptIn(ExperimentalTestApi::class)
class SegmentedControlUiTest {

    @Test
    fun clickingSegment_firesCallbackWithCorrectIndex() = runComposeUiTest {
        var lastIndex: Int? = null
        setContent {
            MaterialTheme {
                var selected by remember { mutableStateOf(0) }
                SegmentedControl(
                    items = listOf("Day", "Week", "Month"),
                    selectedIndex = selected,
                    onSelectionChange = { selected = it; lastIndex = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        onNodeWithTag("segment_2").performClick()
        assertEquals(2, lastIndex)
        onNodeWithTag("segment_0").performClick()
        assertEquals(0, lastIndex)
        onNodeWithTag("segment_1").performClick()
        assertEquals(1, lastIndex)
    }

    @Test
    fun disabledSegment_doesNotFireCallback() = runComposeUiTest {
        var lastIndex: Int? = null
        setContent {
            MaterialTheme {
                SegmentedControl(
                    items = listOf(
                        SegmentItem("Day"),
                        SegmentItem("Week"),
                        SegmentItem("Month", enabled = false),
                    ),
                    selectedIndex = 0,
                    onSelectionChange = { lastIndex = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        onNodeWithTag("segment_2").performClick()
        assertNull(lastIndex, "a disabled segment must never invoke onSelectionChange")

        onNodeWithTag("segment_1").performClick()
        assertEquals(1, lastIndex, "enabled segments still fire the callback")
    }

    @Test
    fun initialSelectedIndex_rendersSelectedState() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SegmentedControl(
                    items = listOf("Day", "Week", "Month"),
                    selectedIndex = 1,
                    onSelectionChange = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        onNodeWithTag("segment_0").assertIsNotSelected()
        onNodeWithTag("segment_1").assertIsSelected()
        onNodeWithTag("segment_2").assertIsNotSelected()
    }

    @Test
    fun singleItem_rendersWithoutCrash() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SegmentedControl(
                    items = listOf("Only"),
                    selectedIndex = 0,
                    onSelectionChange = {},
                )
            }
        }
        onNodeWithTag("segment_0").assertIsSelected()
    }

    @Test
    fun emptyItems_throwsIllegalArgumentException() = runComposeUiTest {
        assertFailsWith<IllegalArgumentException> {
            setContent {
                MaterialTheme {
                    SegmentedControl(
                        items = emptyList<String>(),
                        selectedIndex = 0,
                        onSelectionChange = {},
                    )
                }
            }
        }
    }

    @Test
    fun longLabels_renderInEqualWidthMode() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SegmentedControl(
                    items = listOf(
                        "A very long label that should truncate",
                        "Another quite long label here",
                    ),
                    selectedIndex = 0,
                    onSelectionChange = {},
                    width = SegmentedControlWidth.Equal,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        onNodeWithTag("segment_0").assertIsSelected()
        onNodeWithTag("segment_1").assertIsNotSelected()
    }

    @Test
    fun longLabels_renderInContentWidthMode() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SegmentedControl(
                    items = listOf("Short", "A noticeably longer label"),
                    selectedIndex = 1,
                    onSelectionChange = {},
                    width = SegmentedControlWidth.Content,
                )
            }
        }
        onNodeWithTag("segment_0").assertIsNotSelected()
        onNodeWithTag("segment_1").assertIsSelected()
    }

    @Test
    fun iconOnlySegments_areClickable() = runComposeUiTest {
        var lastIndex: Int? = null
        setContent {
            MaterialTheme {
                SegmentedControl(
                    items = listOf(
                        SegmentItem(icon = blankIcon()),
                        SegmentItem(icon = blankIcon()),
                    ),
                    selectedIndex = 0,
                    onSelectionChange = { lastIndex = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        onNodeWithTag("segment_1").performClick()
        assertEquals(1, lastIndex)
    }

    /** A minimal, dependency-free [ImageVector] for exercising icon-only segments. */
    private fun blankIcon(): androidx.compose.ui.graphics.vector.ImageVector =
        androidx.compose.ui.graphics.vector.ImageVector.Builder(
            defaultWidth = androidx.compose.ui.unit.Dp(24f),
            defaultHeight = androidx.compose.ui.unit.Dp(24f),
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).build()
}
