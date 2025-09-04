package com.example.fifteenpuzzlegame.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.fifteenpuzzlegame.R
import com.example.fifteenpuzzlegame.databinding.ActivityGameBinding
import com.example.fifteenpuzzlegame.databinding.DialogGameOverBinding

class GameActivity : AppCompatActivity() {

     lateinit var binding: ActivityGameBinding
     lateinit var gridLayout: GridLayout
     var  numbers: MutableList<Int> = mutableListOf()
     lateinit var tiles: MutableList<Button>
     var emptyTileIndex = 15
     var countMoves = 0
     var seconds = 0
     var isRunning = false
     var isPaused = false
     val handler = Handler(Looper.getMainLooper())
     val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                seconds++
                val hours = seconds / 3600
                val mins = seconds / 60
                val secs = seconds % 60
                val timeText = when {
                    hours > 0 -> String.format("Time : %02d:%02d:%02d", hours, mins, secs)
                    mins > 0 -> String.format("Time : %02d:%02d", mins, secs)
                    else -> "Time : ${secs}s"
                }

                binding.txtTimer.text = timeText
                handler.postDelayed(this, 1000)
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTimer()

        gridLayout = binding.gridLayout
        initList()

        shuffleNumber()
        updateGridUI()
        binding.txtRestart.setOnClickListener {
            restartGame()
        }

        binding.txtPasuse.setOnClickListener {
            if (!isPaused) {
                stopTimer()
                binding.txtPasuse.text = "Resume"
                isPaused = true
            } else {
                startTimer()
                binding.txtPasuse.text = "Pause"
                isPaused = false
            }
        }

    }

     fun initList(){
        numbers.clear()
        numbers.addAll(1..15)
        numbers.add(0)
    }

     fun isAdjacent(index1: Int, index2: Int): Boolean {
        val row1 = index1 / 4
        val col1 = index1 % 4
        val row2 = index2 / 4
        val col2 = index2 % 4
        return (row1 == row2 && Math.abs(col1 - col2) == 1) ||
                (col1 == col2 && Math.abs(row1 - row2) == 1)
    }

     fun swapTiles(i: Int, j: Int) {
        val tempText = tiles[i].text
        tiles[i].text = tiles[j].text
        tiles[j].text = tempText
    }

     fun restartGame(){
        countMoves = 0
        binding.txtMoveCounter.text = "Moves: $countMoves"
        resetTimer()
        shuffleNumber()
        updateGridUI()
        startTimer()
    }

     fun shuffleNumber(){
        numbers.shuffle()
    }

     fun updateGridUI(){
        gridLayout.removeAllViews()
        tiles = mutableListOf()

        for (i in 0 until 16) {
            val tile = Button(this)
            val value = numbers[i]

            tile.text = if (value == 0) "" else value.toString()
            tile.textSize = 18f
            tile.setTextColor(Color.WHITE)
            tile.setBackgroundColor(getColor(R.color.primary_color))
            tile.setPadding(8, 8, 8, 8)
            tile.typeface= ResourcesCompat.getFont(this, R.font.cherry_bomb_one)
            val sizeInDp = 80
            val scale = resources.displayMetrics.density
            val sizeInPx = (sizeInDp * scale).toInt()

            val params = GridLayout.LayoutParams().apply {
                width = sizeInPx
                height = sizeInPx
                setMargins(8, 8, 8, 8)
            }

            gridLayout.addView(tile, params)
            tiles.add(tile)

            if (value == 0) emptyTileIndex = i

            tile.setOnClickListener {
                val clickedIndex = i
                if (isAdjacent(clickedIndex, emptyTileIndex)) {
                    swapTiles(clickedIndex, emptyTileIndex)
                    emptyTileIndex = clickedIndex
                    countMoves++
                    binding.txtMoveCounter.text = "Moves: $countMoves"

                    if (checkWin()){
                        val dialogbinding: DialogGameOverBinding = DialogGameOverBinding.inflate(layoutInflater)
                        val builder = AlertDialog.Builder(this)
                            .setView(dialogbinding.root)

                        val dialog = builder.create()
                        dialog.setContentView(dialogbinding.root)
                        dialog.show()
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.window?.attributes?.windowAnimations= R.style.DialogAnimation
                        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        dialog.window?.setDimAmount(0.6f)

                        dialogbinding.btnExit.setOnClickListener { finish() }
                        dialogbinding.btnPlayAgain.setOnClickListener {
                            dialog.dismiss()
                            Handler(mainLooper).postDelayed({
                                restartGame()
                            },2000)
                        }

                    }
                }
            }
    }}

    fun checkWin(): Boolean{
        for (i in 0 until 15){
            val value= tiles[i].text.toString()
            if (value != (i+1).toString()){
                return false
            }
        }
        return tiles[15].text.toString().isEmpty()
    }

    fun startTimer(){
        isRunning = true
        handler.post(runnable)
    }

     fun stopTimer() {
        isRunning = false
        handler.removeCallbacks(runnable)
    }

     fun resetTimer() {
        stopTimer()
        seconds = 0
        binding.txtTimer.text = "Time : 0s"
    }

    override fun onPause() {
        stopTimer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (!isRunning){
            startTimer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}