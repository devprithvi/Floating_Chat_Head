package com.ipie.floating_chat_head

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ipie.floating_chat_head.Services.FloatingBubblePermissions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FloatingBubblePermissions.startPermissionRequest(this)
        val start = findViewById<Button>(R.id.startButton)

        //// Intent for dialer pad
        start.setOnClickListener {
            //// Intent for start for the start service
            val intent = Intent(applicationContext, SimpleService::class.java)
            ContextCompat.startForegroundService(this, intent)
            //// Intent for dialer pad
            val intent1 = Intent(Intent.ACTION_DIAL)
            startActivity(intent1)
            finish()
        }
    }
}