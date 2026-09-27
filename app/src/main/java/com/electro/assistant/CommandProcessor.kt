package com.electro.assistant

import android.content.Context
import androidx.fragment.app.FragmentActivity

/**
 * Reads the trigger words from Settings (so you can change them any time)
 * and decides what to do with what you said.
 */
class CommandProcessor(private val activity: FragmentActivity) {

    private val prefs = Settings(activity)
    private val appLauncher = AppLauncher(activity)
    private val logger = CommandLogger(activity)

    fun process(spokenText: String, onResult: (String) -> Unit) {
        val text = spokenText.trim().lowercase()

        val openWord = prefs.openTrigger()      // default "open"
        val playWord = prefs.playTrigger()       // default "play"
        val unlockWord = prefs.unlockTrigger()   // default "unlock"

        when {
            text.startsWith(unlockWord) -> {
                BiometricUnlockHelper(activity).requestUnlock(
                    onSuccess = {
                        logger.log(spokenText, "Unlock confirmed via device biometric/PIN")
                        onResult("Unlocked")
                    },
                    onFailure = { reason ->
                        logger.log(spokenText, "Unlock failed: $reason")
                        onResult("Could not unlock: $reason")
                    }
                )
            }

            text.startsWith("$playWord ") && text.contains("youtube") -> {
                val query = text
                    .removePrefix("$playWord ")
                    .replace("on youtube", "")
                    .replace("youtube", "")
                    .trim()
                appLauncher.playOnYoutube(query)
                logger.log(spokenText, "Played on YouTube: $query")
                onResult("Playing \"$query\" on YouTube")
            }

            text.startsWith("$openWord ") -> {
                val appName = text.removePrefix("$openWord ").trim()
                val opened = appLauncher.openAppByName(appName)
                val outcome = if (opened) "Opened $appName" else "Could not find an app named \"$appName\""
                logger.log(spokenText, outcome)
                onResult(outcome)
            }

            else -> {
                logger.log(spokenText, "Not recognized")
                onResult("Sorry, I didn't understand that command.")
            }
        }
    }
}
