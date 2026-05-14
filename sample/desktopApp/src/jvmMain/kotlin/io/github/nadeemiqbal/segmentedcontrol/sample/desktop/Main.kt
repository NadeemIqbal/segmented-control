package io.github.nadeemiqbal.segmentedcontrol.sample.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.nadeemiqbal.segmentedcontrol.sample.SampleApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SegmentedControl Sample",
        state = rememberWindowState(width = 480.dp, height = 800.dp),
    ) {
        SampleApp()
    }
}
