package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var messageInput: EditText
    private lateinit var sendInternalButton: Button
    private lateinit var sendExternalButton: Button

    private val handler = Handler(Looper.getMainLooper())
    private var startTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Прив'язки
        timerText = findViewById(R.id.timer_text)
        messageInput = findViewById(R.id.message_input)
        sendInternalButton = findViewById(R.id.send_internal_button)
        sendExternalButton = findViewById(R.id.send_external_button)

        // Відновлення або ініціалізація часу старту
        startTime = savedInstanceState?.getLong("start_time") ?: System.currentTimeMillis()

        // Запуск таймера
        runTimer()

        // Надсилання у нове вікно (додаткова активність)
        sendInternalButton.setOnClickListener {
            val message = messageInput.text.toString()
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("message_key", message)
                putExtra("start_time", startTime)
            }
            startActivity(intent)
        }

        // Надсилання через інші додатки
        sendExternalButton.setOnClickListener {
            val message = messageInput.text.toString()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            val chooser = Intent.createChooser(intent, "Поділитися через...")
            startActivity(chooser)
        }
    }

    // Збереження часу запуску при повороті або відновленні
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("start_time", startTime)
        super.onSaveInstanceState(outState)
    }

    // Сам таймер, який оновлює TextView кожну секунду
    private fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                val elapsedMillis = System.currentTimeMillis() - startTime
                val totalSeconds = (elapsedMillis / 1000).toInt()
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                timerText.text = String.format("Таймер: %02d:%02d", minutes, seconds)
                handler.postDelayed(this, 1000)
            }
        })
    }
}
