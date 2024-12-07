package com.android.snake2025

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var layoutMain: LinearLayout

    private val tileSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutMain = findViewById(R.id.main)

        // TODO generate game board according to tileSize in code
        initBoard()
    }

    private fun initBoard() {
        for (i in 0 until tileSize) {
            val row = LinearLayout(this)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.weight = 1f
            row.layoutParams = layoutParams
            layoutMain.addView(row)
        }
    }
}