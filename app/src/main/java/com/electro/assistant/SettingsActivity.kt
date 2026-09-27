package com.electro.assistant

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settings = Settings(this)
        val openField: EditText = findViewById(R.id.openTriggerField)
        val playField: EditText = findViewById(R.id.playTriggerField)
        val unlockField: EditText = findViewById(R.id.unlockTriggerField)
        val saveButton: Button = findViewById(R.id.saveSettingsButton)

        openField.setText(settings.openTrigger())
        playField.setText(settings.playTrigger())
        unlockField.setText(settings.unlockTrigger())

        saveButton.setOnClickListener {
            settings.setOpenTrigger(openField.text.toString().trim().ifBlank { "open" })
            settings.setPlayTrigger(playField.text.toString().trim().ifBlank { "play" })
            settings.setUnlockTrigger(unlockField.text.toString().trim().ifBlank { "unlock" })
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
