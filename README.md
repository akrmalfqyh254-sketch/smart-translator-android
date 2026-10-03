# Smart Translator

Smart Translator is a free native Android app built with Kotlin, Jetpack Compose, Material 3, Room, CameraX, OCR, accessibility features, and local-first translation architecture.

Features included in this starter project:
- Arabic RTL UI
- Free local-first translation architecture
- Translation text field and result card
- History and favorites screens
- Settings with dark mode and privacy toggles
- Camera/OCR placeholder screens
- AccessibilityService stub for floating translation actions
- Room database for local translation history
- Manifest declarations for permissions and accessibility support

Project package:
- com.smarttranslator.app
- version: 1.0.0

Requirements:
- Android Studio Ladybug or newer recommended
- JDK 17
- Android SDK 34

Build steps:
1. Open the project in Android Studio.
2. Let Gradle sync the project.
3. Choose a device or emulator.
4. Run the app using the debug build.

Release / Signing:
1. Generate a keystore.
2. Add signing config in app/build.gradle.kts or use Android Studio Generate Signed Bundle/APK.
3. Build a release APK or AAB through Android Studio.

Privacy notes:
- No username or password collection is included in this starter app.
- Accessibility and screen capture are designed to require explicit user consent before network or overlay features are active.
- OCR and translation are local-first and can be swapped for a different provider later.

Notes:
- This project is intentionally structured as a solid foundation for the full translator application you requested.
- The complete floating overlay and screen-capture flow can be extended in production based on your final approval and Android testing environment.
