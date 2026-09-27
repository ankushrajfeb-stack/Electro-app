# Electro — Voice-Controlled Assistant (Android)

## What this does
- **"open [app name]"** — finds and launches any installed app by its visible name.
- **"play [something] on youtube"** — opens YouTube and searches/plays it.
- **"unlock"** — pops up your phone's own fingerprint/face/PIN prompt (see note below).
- **Command history** — every command heard + what Electro did is saved on-device; view, delete one, or wipe all, anytime, from the "View Command History" screen.
- **Settings** — change the trigger words ("open", "play", "unlock") to anything you like.

## Important: about the "unlock" feature
No app — including this one — is allowed to read, store, or bypass your actual PIN, password, or pattern. That's an Android security rule, not something I chose to leave out. What Electro does instead is trigger **Android's own** biometric/PIN prompt (`BiometricPrompt`), and Android reports back only "success" or "failed." Your real credential never touches Electro's code or logs. This is the standard, safe way any app is allowed to ask "is this really you?"

## About your data
- The command log is a plain file stored in Electro's private app storage on your phone only. There's no server in this build — nothing is uploaded anywhere.
- If you want it backed up somewhere too, that would need adding a specific backend (e.g. your own Google Drive or Firebase) — tell me if you want that added and I'll wire it in.

## How to build it
1. Install [Android Studio](https://developer.android.com/studio).
2. **File → New → New Project → Empty Views Activity**, language **Kotlin**, name it "Electro", package name `com.electro.assistant`, minimum SDK 26.
3. Replace the generated `app/build.gradle.kts` and `AndroidManifest.xml` with the ones in this folder.
4. Copy all files from `app/src/main/java/com/electro/assistant/` into your project's matching folder.
5. Copy all files from `app/src/main/res/layout/` and `res/values/` into your project's matching folders.
6. Click **Run** ▶ with your phone connected (USB debugging enabled) or an emulator.
7. On first launch, tap the mic — Android will ask for microphone permission.

## Extending it
Everything is deliberately simple and in separate files so you can extend it:
- Add new commands in `CommandProcessor.kt`.
- Add new settings fields in `Settings.kt` + `activity_settings.xml`.
- Want screen-recording of what Electro does (not just a text log)? That needs the `MediaProjection` API and is a bigger addition — let me know and I'll add it as a next step.
