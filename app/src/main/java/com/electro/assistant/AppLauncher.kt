package com.electro.assistant

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri

class AppLauncher(private val context: Context) {

    /**
     * Tries to open an installed app whose visible name roughly matches
     * what you said (e.g. "open whatsapp", "open camera").
     * Returns true if something was launched.
     */
    fun openAppByName(spokenName: String): Boolean {
        val pm = context.packageManager
        val target = spokenName.trim().lowercase()

        val apps: List<ApplicationInfo> = pm.getInstalledApplications(0)
        var bestMatch: ApplicationInfo? = null

        for (app in apps) {
            val label = pm.getApplicationLabel(app).toString().lowercase()
            if (label == target) {
                bestMatch = app
                break
            }
            if (label.contains(target) && bestMatch == null) {
                bestMatch = app
            }
        }

        val chosen = bestMatch ?: return false
        val launchIntent = pm.getLaunchIntentForPackage(chosen.packageName) ?: return false
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
        return true
    }

    /**
     * Opens YouTube and searches/plays the given query
     * (e.g. "play lofi hip hop on youtube" -> query = "lofi hip hop").
     */
    fun playOnYoutube(query: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:results?q=${Uri.encode(query)}"))
        intent.setPackage("com.google.android.youtube")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // YouTube app not installed - fall back to the browser
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/results?search_query=${Uri.encode(query)}")
            )
            webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(webIntent)
        }
    }
}
