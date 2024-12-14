package com.android.snake2025

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.VibratorManager
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.atan2
import java.util.Random

class MainActivity : AppCompatActivity(), View.OnTouchListener, GestureDetector.OnGestureListener {

    private lateinit var layoutMain: LinearLayout
    private lateinit var gestureDetector: GestureDetector
    private lateinit var layoutScore: TextView
    private lateinit var vibratorManager: VibratorManager

    private val snakeTiles = mutableListOf<SnakeTile>()
    private val rows: MutableList<LinearLayout> = ArrayList()
    private val tileSize = 20

    private var snakeX = tileSize / 2
    private var snakeY = tileSize / 2
    private var snakeSpeed = 200L
    private var snakeLength = 0
    private var snakeDirectionX = 0
    private var snakeDirectionY = 0
    private var appleX = 0
    private var appleY = 0
    private var appleColor = 0
    private var score = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        gestureDetector = GestureDetector(this, this)
        layoutScore = findViewById(R.id.score)
        layoutMain = findViewById(R.id.main)
        layoutMain.setOnTouchListener(this)

        assignRandomApplePosition()
        initBoard()
        gameLoop()
    }

    /**
     * Performs the main tasks of the game and draws objects.
     * This method is called multiple times a second.
     */
    private fun gameLoop() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object  : Runnable {
            override fun run() {
                snakeX += snakeDirectionX
                snakeY += snakeDirectionY

                if (!isGameOver()) {
                    updateApple()
                    drawBoard()
                    mainHandler.postDelayed(this, snakeSpeed)
                } else {
                    MediaPlayer.create(baseContext, R.raw.beep).start()
                    layoutScore.text = String.format(getString(R.string.score_game_over), score.toString())
                }
            }
        })
    }

    /**
     * This method checks for an apple collision.
     */
    private fun updateApple() {
        if (appleX == snakeX && appleY == snakeY) {
            assignRandomApplePosition()
            snakeLength++
            score += 5
            if (score % 20 == 0) {
                snakeSpeed -= 10
            }
            vibratorManager.defaultVibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
            layoutScore.text = String.format(getString(R.string.score), score.toString())
        }
    }

    /**
     * Assigns the apple a random position on the screen.
     */
    private fun assignRandomApplePosition() {
        val random = Random()
        appleColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        appleX = random.nextInt(tileSize)
        appleY = random.nextInt(tileSize)
    }

    /**
     * This method checks if there is a wall or a tail collision.
     */
    private fun isGameOver(): Boolean {
        // Do not check at game start, when user has not pressed any keys.
        if (snakeDirectionX == 0 && snakeDirectionY == 0) {
            return false
        }

        // Check for wall collision
        if (snakeX < 0 || snakeX >= tileSize || snakeY < 0 || snakeY >= tileSize) {
            return true
        }

        // Check for tail collision
        for (snakeTile in snakeTiles) {
            if (snakeTile.x == snakeX && snakeTile.y == snakeY) {
                return true
            }
        }

        return false
    }

    /**
     * Changes the background color of all tiles.
     */
    private fun drawBoard() {
        for (x in 0 until tileSize) {
            for (y in 0 until tileSize) {
                var color = getColor(R.color.blue)
                if (x == appleX && y == appleY) {
                    color = appleColor
                } else if (x == snakeX && y == snakeY) {
                    color = Color.WHITE
                }

                rows[y].getChildAt(x).setBackgroundColor(color)
            }
        }

        for(snakeTile in snakeTiles) {
            rows[snakeTile.y].getChildAt(snakeTile.x).setBackgroundColor(Color.WHITE)
        }

        snakeTiles.add(SnakeTile(snakeX, snakeY))

        if (snakeTiles.size > snakeLength) {
            snakeTiles.removeAt(0)
        }
    }

    /**
     * Creates Linear Layouts according to the tileSize val.
     * Adds all layouts to rows Array List to create a game board.
     */
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

    @SuppressLint("ClickableViewAccessibility")
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
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }
}