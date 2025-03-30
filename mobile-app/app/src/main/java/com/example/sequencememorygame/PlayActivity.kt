package com.example.sequencememorygame

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayActivity : AppCompatActivity() {
    // Player information
    private var playerName: String = "Player"
    private var score: Int = 0

    // Game state variables (Start as lvl 1 and select 4 tiles)
    private var successfulRounds: Int = 0
    private var tilesToSelect: Int = 4
    private var tilesSelected: Int = 0

    // UI
    private lateinit var gridLayout: GridLayout
    private lateinit var txtvPlayerName: TextView
    private lateinit var txtvScore: TextView
    private lateinit var txtvTimer: TextView
    private lateinit var txtvMessage: TextView
    private lateinit var btnBack: Button

    // Game data
    private val tiles = ArrayList<Button>()
    private val highlightedTiles = ArrayList<Int>()
    private val selectedTiles = ArrayList<Int>()

    // Timers
    private var countDownTimer: CountDownTimer? = null
    private val handler = Handler(Looper.getMainLooper())

    // Game state flags
    private var isShowingPattern = false
    private var isGameActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        // Get player name from Intent
        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"

        //Set UI
        gridLayout = findViewById(R.id.gridLayout)
        txtvPlayerName = findViewById(R.id.txtvPlayerName)
        txtvScore = findViewById(R.id.txtvScore)
        txtvTimer = findViewById(R.id.txtvTimer)
        txtvMessage = findViewById(R.id.txtvMessage)
        btnBack = findViewById(R.id.btnBack)

        // Set player name
        txtvPlayerName.text = playerName

        // Create grid
        createGrid()

        btnBack.setOnClickListener {
            // Cancel any running timers before going back
            countDownTimer?.cancel()
            handler.removeCallbacksAndMessages(null)

            //back to main menu
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Start the game
        startNewRound()
    }

    // Create grid with buttons
    private fun createGrid() {
        // Create 36 buttons (6x6 grid)
        for (i in 0 until 36) {
            val button = Button(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 170
                    height = 170
                    setMargins(4, 4, 4, 4)
                }
                setBackgroundColor(Color.BLUE)
                id = i

                // Set click listener for each tile
                setOnClickListener {
                    // Allow clicks when not showing pattern and game is active
                    if (!isShowingPattern && isGameActive) {
                        onTileClick(i)
                    }
                }
            }

            // Add the button to list and to the grid
            tiles.add(button)
            gridLayout.addView(button)
        }
    }

    // Start round
    private fun startNewRound() {
        // Update game state
        isGameActive = false
        isShowingPattern = true

        // Clear previous selections
        highlightedTiles.clear()
        selectedTiles.clear()
        tilesSelected = 0

        // Reset tile colors
        for (tile in tiles) {
            tile.setBackgroundColor(Color.BLUE)
        }

        // After 3 rounds, require 5 tiles
        tilesToSelect = if (successfulRounds >= 3) 5 else 4

        // Update message
        txtvMessage.text = "Memorize the pattern!"

        // Select random tiles
        val tileIndices = (0 until 36).shuffled().take(tilesToSelect)
        highlightedTiles.addAll(tileIndices)

        // Highlight the tiles
        for (index in highlightedTiles) {
            tiles[index].setBackgroundColor(Color.WHITE)
        }

        // After 3 seconds, hide the pattern and let the user select
        handler.postDelayed({

            // Reset tile colors
            for (tile in tiles) {
                tile.setBackgroundColor(Color.BLUE)
            }

            // Update game state
            isShowingPattern = false
            isGameActive = true

            // Start the 5-second countdown timer
            startCountDownTimer()

            // Update message
            txtvMessage.text = "Now select the highlighted tiles!"

        }, 3000)
    }

    // Start countdown timer
    private fun startCountDownTimer() {

        // Create a new timer for 5 seconds
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update timer display each second
                txtvTimer.text = "Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                txtvTimer.text = "Time: 0"
                gameOver("Time's up!")
            }
        }.start()
    }

    // Handle tile click
    private fun onTileClick(tileIndex: Int) {
        // Don't allow clicks if game isn't active
        if (!isGameActive) return

        // Don't allow selecting the same tile twice
        if (selectedTiles.contains(tileIndex)) return

        // Record selection
        selectedTiles.add(tileIndex)
        tilesSelected++

        // Change the tile color to indicate selection
        tiles[tileIndex].setBackgroundColor(Color.WHITE)

        // Check if the selected tile is wrong
        if (!highlightedTiles.contains(tileIndex)) {
            tiles[tileIndex].setBackgroundColor(Color.RED)
            gameOver("Wrong tile selected!")
            return
        }

        // Check if all tiles have been selected
        if (tilesSelected == tilesToSelect) {
            // Cancel timer
            countDownTimer?.cancel()

            // Award points (standard 10, else 20)
            val roundPoints = if (tilesToSelect > 4) 20 else 10
            score += roundPoints
            txtvScore.text = "Score: $score"

            // Increment successful rounds counter
            successfulRounds++

            // Short delay before next round
            handler.postDelayed({
                startNewRound()
            }, 1500)
        }
    }

    // Handle game over
    private fun gameOver(message: String) {
        // Cancel timers
        countDownTimer?.cancel()
        isGameActive = false
        isShowingPattern = false

        // Show all correct tiles in white
        for (index in highlightedTiles) {
            tiles[index].setBackgroundColor(Color.WHITE)
        }

        // Save score if player has a name
        if (playerName.isNotBlank()) {
            // Create a ScoreManager instance
            val scoreManager = ScoreManager(this)

            // Save the player's name and score
            scoreManager.addScore(playerName, score)
        }

        // Show game over dialog
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("$message\nYour final score: $score")
            // Go back to main menu
            .setPositiveButton("Main Menu") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
            .show()
    }

    // Clean up everything when destroyed
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        handler.removeCallbacksAndMessages(null)
    }
}