package com.electro.assistant

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Every voice command Electro hears and acts on gets one entry here:
 * the raw text, what action was taken, and a timestamp.
 *
 * This is stored ONLY in a local file on the phone (app-private storage,
 * not readable by other apps, not sent anywhere). No server is included
 * in this build. You decide from the Log screen whether to keep it or
 * permanently wipe it.
 */
data class CommandEntry(val id: Long, val timestamp: String, val heardText: String, val actionTaken: String)

class CommandLogger(context: Context) {

    private val logFile = File(context.filesDir, "electro_command_log.json")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun log(heardText: String, actionTaken: String) {
        val entries = readAll().toMutableList()
        val entry = CommandEntry(
            id = System.currentTimeMillis(),
            timestamp = dateFormat.format(Date()),
            heardText = heardText,
            actionTaken = actionTaken
        )
        entries.add(entry)
        writeAll(entries)
    }

    fun readAll(): List<CommandEntry> {
        if (!logFile.exists()) return emptyList()
        val text = logFile.readText()
        if (text.isBlank()) return emptyList()
        val arr = JSONArray(text)
        val out = mutableListOf<CommandEntry>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            out.add(CommandEntry(o.getLong("id"), o.getString("timestamp"), o.getString("heardText"), o.getString("actionTaken")))
        }
        return out
    }

    /** Permanently deletes a single entry. Cannot be undone. */
    fun deleteOne(id: Long) {
        val remaining = readAll().filter { it.id != id }
        writeAll(remaining)
    }

    /** Permanently wipes the entire history. Cannot be undone. */
    fun deleteAll() {
        if (logFile.exists()) logFile.delete()
    }

    private fun writeAll(entries: List<CommandEntry>) {
        val arr = JSONArray()
        for (e in entries) {
            val o = JSONObject()
            o.put("id", e.id)
            o.put("timestamp", e.timestamp)
            o.put("heardText", e.heardText)
            o.put("actionTaken", e.actionTaken)
            arr.put(o)
        }
        logFile.writeText(arr.toString())
    }
}
