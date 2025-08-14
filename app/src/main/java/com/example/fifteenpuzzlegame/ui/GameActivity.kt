package com.example.fifteenpuzzlegame.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fifteenpuzzlegame.R
import com.example.fifteenpuzzlegame.R.color
import com.example.fifteenpuzzlegame.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
     lateinit var binding: ActivityGameBinding

     lateinit var gridLayout: GridLayout
     var  numbers: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gridLayout = binding.gridLayout
        initList()
        shuffleNumber()
        updateGridUI()
        binding.txtRestart.setOnClickListener {
            restartGame()
        }
    }

    private lateinit var tiles: MutableList<Button>
    private var emptyTileIndex = 15


    private fun initList(){
        numbers.clear()
        numbers.addAll(1..15)
        numbers.add(0)
    }

    private fun isAdjacent(index1: Int, index2: Int): Boolean {
        val row1 = index1 / 4
        val col1 = index1 % 4
        val row2 = index2 / 4
        val col2 = index2 % 4
        return (row1 == row2 && Math.abs(col1 - col2) == 1) ||
                (col1 == col2 && Math.abs(row1 - row2) == 1)
    }

    private fun swapTiles(i: Int, j: Int) {
        val tempText = tiles[i].text
        tiles[i].text = tiles[j].text
        tiles[j].text = tempText
    }

    private fun restartGame(){
        shuffleNumber()
        updateGridUI()
    }

    private fun shuffleNumber(){
        numbers.shuffle()
    }

    private fun updateGridUI(){
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
                }
            }
    }}

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}