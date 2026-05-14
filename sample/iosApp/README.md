# iOS sample

A thin SwiftUI host that embeds the shared Compose UI from `:sample:composeApp`.

## Run it

1. Build the shared framework at least once so Xcode can resolve it:
   ```
   ./gradlew :sample:composeApp:embedAndSignAppleFrameworkForXcode
   ```
2. Open `iosApp.xcodeproj` in Xcode.
3. Set your signing **Team** in `Configuration/Config.xcconfig` (`TEAM_ID=...`), or pick a team in
   Xcode's *Signing & Capabilities* tab.
4. Select an iOS Simulator and press **Run**.

The *Compile Kotlin Framework* build phase re-invokes Gradle on every build, so changes to the
Kotlin/Compose code are picked up automatically.

The Compose entry point is `MainViewController()` in
`composeApp/src/iosMain/.../MainViewController.kt`, surfaced to Swift as `MainViewControllerKt`.
