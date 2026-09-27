package com.electro.assistant

import android.content.Context

/**
 * All the "control words" Electro listens for live here, and are editable
 * from SettingsActivity - nothing is hardcoded/locked.
 */
class Settings(context: Context) {
    private val prefs = context.getSharedPreferences("electro_settings", Context.MODE_PRIVATE)

    fun openTrigger() = prefs.getString("open_trigger", "open") ?: "open"
    fun playTrigger() = prefs.getString("play_trigger", "play") ?: "play"
    fun unlockTrigger() = prefs.getString("unlock_trigger", "unlock") ?: "unlock"

    fun setOpenTrigger(v: String) = prefs.edit().putString("open_trigger", v).apply()
    fun setPlayTrigger(v: String) = prefs.edit().putString("play_trigger", v).apply()
    fun setUnlockTrigger(v: String) = prefs.edit().putString("unlock_trigger", v).apply()
}
