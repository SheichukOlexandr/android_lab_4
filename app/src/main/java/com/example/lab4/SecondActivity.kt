package com.example.lab4

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var messageText: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var startTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Вертикальний LinearLayout як у MainActivity
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Таймер згори
        timerText = TextView(this).apply {
            text = "Таймер: 00:00"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            setPadding(0, 30, 0, 0)
        }

        // Повідомлення нижче
        messageText = TextView(this).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            setPadding(0, 24, 0, 0)
        }

        layout.addView(timerText)
        layout.addView(messageText)

        setContentView(layout)

        // Отримання часу та повідомлення
        startTime = intent.getLongExtra("start_time", System.currentTimeMillis())
        val message = intent.getStringExtra("message_key") ?: ""

        // Встановлення повідомлення
        messageText.text = "Отримане повідомлення:\n$message"

        // Запуск таймера
        runTimer()
    }

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
