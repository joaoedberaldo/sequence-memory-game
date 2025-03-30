package com.example.sequencememorygame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class HighScoreActivity: AppCompatActivity() {

    // UI
    private lateinit var txtvFirstPlace: TextView
    private lateinit var txtvFirstScore: TextView
    private lateinit var txtvSecondPlace: TextView
    private lateinit var txtvSecondScore: TextView
    private lateinit var txtvThirdPlace: TextView
    private lateinit var txtvThirdScore: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)

        txtvFirstPlace = findViewById(R.id.txtvFirstPlace)
        txtvFirstScore = findViewById(R.id.txtvFirstScore)
        txtvSecondPlace = findViewById(R.id.txtvSecondPlace)
        txtvSecondScore = findViewById(R.id.txtvSecondScore)
        txtvThirdPlace = findViewById(R.id.txtvThirdPlace)
        txtvThirdScore = findViewById(R.id.txtvThirdScore)
        backButton = findViewById(R.id.btnBack)

        // Load high scores
        loadHighScores()

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //Load highScores from ScoreManager
    private fun loadHighScores() {
        // Get the score manager
        val scoreManager = ScoreManager(this)

        // Get the top scores
        val topScores = scoreManager.getTopScores()

        // Display first place
        if (topScores.size >= 1) {
            val (name, score) = topScores[0]
            txtvFirstPlace.text = "1. $name"
            txtvFirstScore.text = "Score: $score"
        }

        // Display second place
        if (topScores.size >= 2) {
            val (name, score) = topScores[1]
            txtvSecondPlace.text = "2. $name"
            txtvSecondScore.text = "Score: $score"
        }

        // Display third place
        if (topScores.size >= 3) {
            val (name, score) = topScores[2]
            txtvThirdPlace.text = "3. $name"
            txtvThirdScore.text = "Score: $score"
        }
    }
}