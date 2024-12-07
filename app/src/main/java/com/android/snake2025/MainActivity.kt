package com.android.snake2025

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.Random


class MainActivity : AppCompatActivity() {

    private lateinit var layoutMain: LinearLayout
    private val rows: MutableList<LinearLayout> = ArrayList()

    private val tileSize = 20
    private var snakeX = tileSize / 2
    private var snakeY = tileSize / 2

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutMain = findViewById(R.id.main)

        // TODO generate game board according to tileSize in code
        initBoard()
        gameLoop()
    }

    private fun gameLoop() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object  : Runnable {
            override fun run() {
                drawBoard()
                mainHandler.postDelayed(this, 500)
            }
        })
    }

    private fun drawBoard() {
        // TODO set background color for cells
        // TODO set background color for snake
        // TODO move snake in one direction
        for (x in 0 until tileSize) {
            for (y in 0 until tileSize) {
                var color = Color.BLACK
                if (x == snakeX && y == snakeY) {
                    color = Color.WHITE
                }

                rows[y].getChildAt(x).setBackgroundColor(color)
            }
        }

        snakeY--
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
            rows.add(row)

            for (j in 0 until tileSize) {
                val cell = View(this)
                val layoutParamsCell = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParamsCell.weight = 1f
                cell.layoutParams = layoutParamsCell

                val random = Random()
                val color = Color.argb(255, random.nextInt(256), random.nextInt(256),random.nextInt(256))
                cell.setBackgroundColor(color)
                row.addView(cell)
            }
        }

        rows.reverse()
    }
}