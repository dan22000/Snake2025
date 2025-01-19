package com.android.snake2025

import android.content.Context

class Preferences(context: Context) {

    companion object {
        const val HIGH_SCORE = "high_score"
        const val PREFERENCES_NAME = "preferences_snake"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setHighScore(highScore: Int) {
        preferences.edit().putInt(HIGH_SCORE, highScore).apply()
    }

    fun getHighScore(): Int {
        return preferences.getInt(HIGH_SCORE, 0)
    }
}