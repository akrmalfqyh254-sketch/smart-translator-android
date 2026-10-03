# Smart Translator

Smart Translator is a free native Android app for Arabic-first translation with a modern Material 3 UI, local-first translation architecture, OCR image support, accessibility overlay, and local history/favorites.

Application package:
- com.smarttranslator.app
- version: 1.0.0

Main included features:
- Arabic RTL UI
- Translation text box with source/target language selection
- Copy translated text
- Favorites/history screens
- OCR placeholder flow for image translation
- AccessibilityService and floating-translation groundwork
- Room database foundation for translation history
- Settings screen with dark mode and privacy notices
- Local-first translation design, suitable for future engine swapping

Technical stack:
- Kotlin
- Android SDK 34
- Jetpack Compose
- Material 3
- Room
- Android CameraX
- ML Kit OCR / Translation hooks
- AccessibilityService
- MediaProjection foundation

Build requirements:
- Android Studio Iguana / Ladybug or newer
- JDK 17
- Android SDK 34

Quick build steps:
1. Open the project in Android Studio.
2. Allow Gradle to sync.
3. Use a device or emulator with API 23+.
4. Run the app in Debug mode.

Release build and signing:
1. Generate a keystore:
   keytool -genkeypair -v -keystore smart-translator-release.jks -alias smarttranslator -keyalg RSA -keysize 2048 -validity 10000
2. In Android Studio, use Build > Generate Signed Bundle / APK.
3. Or add a signingConfig block in app/build.gradle.kts.
4. Build a Release APK or AAB for Google Play submission.

Privacy and consent requirements:
- No password collection is included.
- Accessibility and screen capture are designed to require explicit user approval.
- The app is local-first and should avoid collecting screen content unless the user opts in.
- The translation engine is designed so you can replace the default placeholder implementation with a real local or remote provider later.

Features planned for full production completion:
- Real translation engine integration (local ML Kit or online free translator backend)
- Speech-to-text using Android recognition APIs
- Text-to-speech playback
- Camera capture with OCR processing
- Accessibility floating bubble overlay with copy/speak/swap/close actions
- Screenshot/media projection translation flow
- Secure release signing and Play Store metadata

Important note:
This repo contains a strong foundation for the requested translator app, but a true APK/AAB bundle still requires a machine with Android SDK + Gradle tooling available to build the signed release artifacts.
