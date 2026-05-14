# SegmentedControl

**An iOS-style segmented control for Compose Multiplatform** — platform-appropriate visuals with
three styles built in, an animated selection indicator, and full keyboard & RTL support.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.nadeemiqbal/segmented-control)](https://central.sonatype.com/artifact/io.github.nadeemiqbal/segmented-control)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Build](https://github.com/NadeemIqbal/segmented-control/actions/workflows/build.yml/badge.svg)](https://github.com/NadeemIqbal/segmented-control/actions/workflows/build.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin)](https://kotlinlang.org)
![Android](https://img.shields.io/badge/Android-24%2B-3DDC84?logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-x64%20%7C%20arm64%20%7C%20simulator-000000?logo=apple&logoColor=white)
![Desktop](https://img.shields.io/badge/Desktop-JVM-007396?logo=openjdk&logoColor=white)
![Web](https://img.shields.io/badge/Web-wasmJs-654FF0?logo=webassembly&logoColor=white)

<!-- TODO: add hero.gif — record the sample app cycling through the iOS / Material3 / Pill styles,
     toggling icon/text modes, and showing a disabled segment. Drop it at docs/hero.gif and
     reference it here. -->

## Why this library

Material 3's `SingleChoiceSegmentedButtonRow` always looks like Material — which feels out of place
on iOS, where users expect the familiar `UISegmentedControl`. `SegmentedControl` gives you a
platform-appropriate look from a single Compose codebase: ship the `iOS` style on Apple platforms,
`Material3` on Android, or `Pill` everywhere — without forking your UI code.

## Platform support

| Platform | Supported | Tested              |
|----------|:---------:|---------------------|
| Android  |     ✅     | ✅ (unit + UI)       |
| iOS      |     ✅     | ✅ (UI, Skiko)       |
| Desktop  |     ✅     | ✅ (unit + UI)       |
| Web      |     ✅     | ✅ (compile + logic) |

## Installation

`gradle/libs.versions.toml`:

```toml
[libraries]
segmented-control = { module = "io.github.nadeemiqbal:segmented-control", version = "0.1.0" }
```

`commonMain` dependencies:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.segmented.control)
        }
    }
}
```

## Quick start

```kotlin
var selected by remember { mutableStateOf(0) }

SegmentedControl(
    items = listOf("Day", "Week", "Month"),
    selectedIndex = selected,
    onSelectionChange = { selected = it },
    modifier = Modifier.fillMaxWidth(),
)
```

## API examples

**Icon + text segments**

```kotlin
SegmentedControl(
    items = listOf(
        SegmentItem("List", Icons.Default.List),
        SegmentItem("Grid", Icons.Default.GridView),
    ),
    selectedIndex = selected,
    onSelectionChange = { selected = it },
)
```

**Pick a style**

```kotlin
SegmentedControl(
    items = items,
    selectedIndex = selected,
    onSelectionChange = { selected = it },
    style = SegmentedControlStyle.iOS,        // iOS | Material3 | Pill
)
```

**Disabled segments**

```kotlin
SegmentedControl(
    items = labels.mapIndexed { i, label -> SegmentItem(label, enabled = i != 2) },
    selectedIndex = selected,
    onSelectionChange = { selected = it },     // never fires for segment 2
)
```

**Equal vs content width**

```kotlin
// Equal: each segment shares the width evenly; long labels ellipsize.
SegmentedControl(items, selected, { selected = it }, width = SegmentedControlWidth.Equal, modifier = Modifier.fillMaxWidth())

// Content: each segment is as wide as its content; the control grows to fit.
SegmentedControl(items, selected, { selected = it }, width = SegmentedControlWidth.Content)
```

**Icon-only**

```kotlin
SegmentedControl(
    items = listOf(
        SegmentItem(icon = Icons.Default.FormatAlignLeft),
        SegmentItem(icon = Icons.Default.FormatAlignCenter),
        SegmentItem(icon = Icons.Default.FormatAlignRight),
    ),
    selectedIndex = selected,
    onSelectionChange = { selected = it },
)
```

**Keyboard navigation** — on Desktop and Web, focus the control (Tab) and use the Left/Right arrow
keys to move the selection between enabled segments. No extra setup required.

## Customization

```kotlin
SegmentedControl(
    items = items,
    selectedIndex = selected,
    onSelectionChange = { selected = it },
    style = SegmentedControlStyle.Pill,
    colors = SegmentedControlDefaults.colors(SegmentedControlStyle.Pill).copy(
        thumbColor = MaterialTheme.colorScheme.tertiary,
        selectedContentColor = MaterialTheme.colorScheme.onTertiary,
    ),
    shape = RoundedCornerShape(8.dp),
    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
)
```

- **`style`** — `iOS`, `Material3` or `Pill`. Drives the track/thumb shape, elevation and dividers.
- **`colors`** — start from `SegmentedControlDefaults.colors(style)` and `.copy(...)` what you need,
  or build a `SegmentedControlColors` from scratch.
- **`shape`** — overrides the track and thumb shape.
- **`animationSpec`** — any `AnimationSpec<Float>`; defaults to a gentle spring.
- **`width`** — `Equal` or `Content`.
- **`enabled`** — `false` makes the whole control inert and muted.

## Comparison with alternatives

| | **SegmentedControl** | Material 3 `SingleChoiceSegmentedButtonRow` | Hand-rolled `Row` |
|---|---|---|---|
| iOS-native look | ✅ built-in `iOS` style | ❌ always Material | ⚠️ you build it |
| Material look | ✅ `Material3` style | ✅ | ⚠️ you build it |
| Animated indicator | ✅ spring, configurable | ⚠️ basic | ❌ DIY |
| Icon / text / both | ✅ | ✅ | ⚠️ DIY |
| Disabled segments | ✅ per-segment | ✅ | ⚠️ DIY |
| Equal & content width | ✅ | ⚠️ equal only | ⚠️ DIY |
| Keyboard navigation | ✅ arrow keys | ❌ | ❌ DIY |
| RTL | ✅ | ✅ | ⚠️ DIY |
| Multiplatform | ✅ Android/iOS/Desktop/Web | ✅ | ✅ |

Be honest with yourself: if you only ship Android and you're all-in on Material, the built-in M3
control is fine. Reach for this library when you want an iOS-appropriate look, a richer animated
indicator, or keyboard support — without maintaining your own component.

## Roadmap

- Scrollable / overflow mode for many segments
- Badge support on segments
- Optional haptic feedback on selection (Android/iOS)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md). Bug reports and feature requests are welcome via GitHub
Issues.

## License

```
Copyright 2026 Nadeem Iqbal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```

See [LICENSE](LICENSE) for the full text.
