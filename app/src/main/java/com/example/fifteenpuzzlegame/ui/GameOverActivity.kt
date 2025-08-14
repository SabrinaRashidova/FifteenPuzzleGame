package com.example.fifteenpuzzlegame.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fifteenpuzzlegame.R
import com.example.fifteenpuzzlegame.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {

     lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}