package com.example.fifteenpuzzlegame

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fifteenpuzzlegame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var gridLayout: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.rootLayout)

        gridLayout = binding.gridLayout
        generateTiles()
    }


    private lateinit var tiles: MutableList<Button>
    private var emptyTileIndex = 15

    private fun generateTiles() {
        val numbers = (1..15).toMutableList()
        numbers.shuffle()
        numbers.add(0)

        tiles = mutableListOf()

        for (i in 0 until 16) {
            val tile = Button(this)
            val value = numbers[i]

            tile.text = if (value == 0) "" else value.toString()
            tile.textSize = 18f
            tile.setTextColor(Color.BLACK)
            tile.setPadding(8, 8, 8, 8)

            val sizeInDp = 80
            val scale = resources.displayMetrics.density
            val sizeInPx = (sizeInDp * scale).toInt()

            val params = GridLayout.LayoutParams().apply {
//                rowSpec = GridLayout.spec(i / 4)
//                columnSpec = GridLayout.spec(i % 4)
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
        }
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

}