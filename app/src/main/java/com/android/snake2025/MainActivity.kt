package com.android.snake2025

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.atan2
import java.util.Random

class MainActivity : AppCompatActivity(), View.OnTouchListener, GestureDetector.OnGestureListener {

    private lateinit var layoutMain: LinearLayout
    private lateinit var gestureDetector: GestureDetector

    private val rows: MutableList<LinearLayout> = ArrayList()
    private val tileSize = 20
    private var snakeX = tileSize / 2
    private var snakeY = tileSize / 2
    private var snakeDirectionX = 0
    private var snakeDirectionY = 0
    private var appleX = 0
    private var appleY = 0


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestureDetector = GestureDetector(this, this)
        layoutMain = findViewById(R.id.main)
        layoutMain.setOnTouchListener(this)

        // TODO assignRandomApplePosition
        assignRandomApplePosition()
        initBoard()
        gameLoop()
    }

    private fun gameLoop() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object  : Runnable {
            override fun run() {
                snakeX += snakeDirectionX
                snakeY += snakeDirectionY

                if (!isGameOver()) {
                    updateApple()
                    drawBoard()
                    mainHandler.postDelayed(this, 500)
                }
            }
        })
    }

    private fun updateApple() {
        if (appleX == snakeX && appleY == snakeY) {
            assignRandomApplePosition()
        }
    }

    private fun assignRandomApplePosition() {
        val random = Random()
        appleX = random.nextInt(tileSize)
        appleY = random.nextInt(tileSize)
    }

    private fun isGameOver(): Boolean {
        if (snakeDirectionX == 0 && snakeDirectionY == 0) {
            return false
        }

        // Check for wall collision
        if (snakeX < 0 || snakeX >= tileSize || snakeY < 0 || snakeY >= tileSize) {
            return true
        }

        return false
    }

    private fun drawBoard() {
        for (x in 0 until tileSize) {
            for (y in 0 until tileSize) {
                var color = Color.BLACK
                if (x == appleX && y == appleY) {
                    color = Color.GREEN
                } else if (x == snakeX && y == snakeY) {
                    color = Color.WHITE
                }

                rows[y].getChildAt(x).setBackgroundColor(color)
            }
        }
    }

    private fun initBoard() {
        for (i in 0 until tileSize) {
            val row = LinearLayout(this)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f
            row.layoutParams = layoutParams

            layoutMain.addView(row)
            rows.add(row)

            for (j in 0 until tileSize) {
                val cell = View(this)
                cell.layoutParams = layoutParams
                row.addView(cell)
            }
        }

        rows.reverse()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        val angle = Math.toDegrees(atan2(p0!!.y - p1.y, p1.x - p0.x).toDouble()).toFloat()

        // User swiped Right
        if (angle > -45 && angle <= 45 && snakeDirectionX == 0) {
            snakeDirectionY = 0
            snakeDirectionX = 1
            return true
        }

        // User swiped Left
        if ((angle >= 135 && angle < 180 || angle < -135 && angle > -180) && snakeDirectionX == 0) {
            snakeDirectionY = 0
            snakeDirectionX = -1
            return true
        }

        // User swiped Down
        if (angle < -45 && angle >= -135 && snakeDirectionY == 0) {
            snakeDirectionY = -1
            snakeDirectionX = 0
            return true
        }

        // User swiped Up
        if (angle > 45 && angle <= 135 && snakeDirectionY == 0) {
            snakeDirectionY = 1
            snakeDirectionX = 0
            return true
        }

        return false
    }

    override fun onLongPress(e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return true
    }
}