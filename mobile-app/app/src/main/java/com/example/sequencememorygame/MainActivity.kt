package com.example.sequencememorygame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.edtUserName)

//      Buttons below
        val instructionsButton = findViewById<Button>(R.id.btnInstructions)
        val playButton = findViewById<Button>(R.id.btnPlay)
        val rankingButton = findViewById<Button>(R.id.btnHighScore)
        val devButton = findViewById<Button>(R.id.btnDev)

        instructionsButton.setOnClickListener {
            val intent = Intent(this, InstructionsActivity::class.java)
            startActivity(intent)
        }

        playButton.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }

        rankingButton.setOnClickListener {
            val intent = Intent(this, HighScoreActivity::class.java)
            startActivity(intent)
        }

        devButton.setOnClickListener {
            val intent = Intent(this, DevActivity::class.java)
            startActivity(intent)
        }

    }
}