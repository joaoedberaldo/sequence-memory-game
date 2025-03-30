package com.example.sequencememorygame

import android.content.Context

class ScoreManager (private val context: Context) {
    // SharedPreferences to store high scores
    private val sharedPreferences = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)

    //get top scores (player + score)
    fun getTopScores(): List<Pair<String, Int>> {
        val allScores = mutableListOf<Pair<String, Int>>()

        // Get the number of scores we have stored
        val scoreCount = sharedPreferences.getInt("score_count", 0)

        // Retrieve all saved scores
        for (i in 0 until scoreCount) {
            val name = sharedPreferences.getString("name_$i", "") ?: ""
            val score = sharedPreferences.getInt("score_$i", 0)

            // Add to our list
            allScores.add(Pair(name, score))
        }

        // Sort by score (descending) and take only top 3
        return allScores.sortedByDescending { it.second }.take(3)
    }

    //save Score
    fun addScore(playerName: String, score: Int) {
        // Don't save scores for unnamed players
        if (playerName.isBlank()) return

        // Get current count of scores
        val currentCount = sharedPreferences.getInt("score_count", 0)

        // Save this new score
        val editor = sharedPreferences.edit()
        editor.putString("name_$currentCount", playerName)
        editor.putInt("score_$currentCount", score)

        // count+1
        editor.putInt("score_count", currentCount + 1)

        // Apply changes
        editor.apply()
    }
}