# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2026-05-14

### Added
- Initial release of `SegmentedControl` for Compose Multiplatform.
- Three visual styles: `iOS`, `Material3`, `Pill`.
- Text-only, icon-only and icon + text segments via `SegmentItem`.
- Animated selection indicator (spring by default, overridable via `animationSpec`).
- Per-segment `enabled` state — disabled segments never fire `onSelectionChange`.
- Equal-width and content-width layout modes (`SegmentedControlWidth`).
- Customizable colors (`SegmentedControlColors` / `SegmentedControlDefaults.colors`) and shape.
- Right-to-left layout support.
- Left/Right arrow-key navigation on Desktop and Web.
- String-based convenience overload alongside the `SegmentItem`-based API.
- Targets: Android, iOS (x64, arm64, simulatorArm64), Desktop (JVM), Web (wasmJs).

[Unreleased]: https://github.com/NadeemIqbal/segmented-control/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/NadeemIqbal/segmented-control/releases/tag/v0.1.0
