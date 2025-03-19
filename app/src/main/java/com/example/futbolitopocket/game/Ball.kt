package com.example.futbolitopocket.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ball(
    modifier: Modifier = Modifier,
    diameter: Float = 20f
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.size(diameter.dp)) {
            drawCircle(color = Color.White)
        }
    }
}
