package com.example.futbolitopocket.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.futbolitopocket.BallGameCanvas

@Composable
fun GameScreen() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Llama a la nueva BallGameCanvas() sin parámetros
            BallGameCanvas()
        }
    }
}
