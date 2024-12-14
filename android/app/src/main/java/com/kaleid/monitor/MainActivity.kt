package com.kaleid.monitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serverAddressInput = findViewById<EditText>(R.id.server_address)
        val startButton = findViewById<Button>(R.id.start_service)

        startButton.setOnClickListener {
            val serverAddress = serverAddressInput.text.toString()
            if (serverAddress.isNotBlank()) {
                val serviceIntent = Intent(this, ForegroundService::class.java).apply {
                    putExtra("SERVER_ADDRESS", serverAddress)
                }
                startService(serviceIntent)
            }
        }
    }
}
