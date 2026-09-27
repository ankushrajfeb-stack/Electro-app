package com.electro.assistant

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class LogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        val logger = CommandLogger(this)
        val listView: ListView = findViewById(R.id.logListView)
        val deleteAllButton: Button = findViewById(R.id.deleteAllButton)

        fun refresh() {
            val entries = logger.readAll().reversed()
            val display = entries.map { "${it.timestamp}\nHeard: \"${it.heardText}\"\n-> ${it.actionTaken}" }
            listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, display)
        }
        refresh()

        // Tap an entry to permanently delete just that one
        listView.setOnItemClickListener { _, _, position, _ ->
            val entries = logger.readAll().reversed()
            val target = entries[position]
            AlertDialog.Builder(this)
                .setTitle("Delete this entry?")
                .setMessage("This permanently removes it. It cannot be undone.")
                .setPositiveButton("Delete") { _, _ -> logger.deleteOne(target.id); refresh() }
                .setNegativeButton("Cancel", null)
                .show()
        }

        deleteAllButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete all history?")
                .setMessage("This permanently wipes your entire command history from this device. It cannot be undone.")
                .setPositiveButton("Delete All") { _, _ -> logger.deleteAll(); refresh() }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
