package com.example.futbolitopocket

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import dev.ricknout.composesensors.accelerometer.getAccelerometerSensor
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState

@Composable
fun BallGameCanvas() {
    // Usando la API de compose-sensors para el acelerómetro
    val sensorValue by rememberAccelerometerSensorValueAsState()

    var score by remember { mutableStateOf(0) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    var ballX by remember { mutableStateOf(0f) }
    var ballY by remember { mutableStateOf(0f) }
    var velocityX by remember { mutableStateOf(0f) }
    var velocityY by remember { mutableStateOf(0f) }
    var isInitialized by remember { mutableStateOf(false) }

    // Parámetros de física
    val sensitivity = 1.5f
    val friction = 0.92f
    val restitution = 0.7f

    // Bucle de actualización (~60 FPS)
    LaunchedEffect(sensorValue) {
        while (true) {
            if (canvasSize.width > 0 && !isInitialized) {
                // Ubica la pelota en el centro al inicio
                ballX = canvasSize.width / 2f
                ballY = canvasSize.height / 2f
                isInitialized = true
            }

            // Desestructuramos el valor del sensor (x, y, z)
            val (accX, accY, _) = sensorValue.value

            // Fuerza derivada del acelerómetro
            val forceX = -accX * sensitivity
            val forceY = accY * sensitivity

            // Actualiza velocidades
            velocityX += forceX
            velocityY += forceY

            // Aplica fricción
            velocityX *= friction
            velocityY *= friction

            // Actualiza posición
            ballX += velocityX
            ballY += velocityY

            delay(16) // ~60 FPS
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Dibujo de la cancha, pelota y marcador
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Guarda el tamaño del Canvas
            canvasSize = size

            val fieldWidth = size.width
            val fieldHeight = size.height

            // Cancha (fondo verde)
            drawRect(
                color = Color.Green,
                topLeft = Offset.Zero,
                size = Size(fieldWidth, fieldHeight)
            )

            // Radio de la pelota
            val ballRadius = 20f

            // Colisiones con los bordes
            if (ballX - ballRadius < 0) {
                ballX = ballRadius
                velocityX = -velocityX * restitution
            } else if (ballX + ballRadius > fieldWidth) {
                ballX = fieldWidth - ballRadius
                velocityX = -velocityX * restitution
            }

            if (ballY - ballRadius < 0) {
                // Verifica si es gol
                val goalWidth = fieldWidth * 0.3f
                val goalLeftX = (fieldWidth - goalWidth) / 2f

                if (ballX in goalLeftX..(goalLeftX + goalWidth)) {
                    // Suma al marcador
                    score++
                    // Resetea la pelota
                    ballX = fieldWidth / 2f
                    ballY = fieldHeight / 2f
                    velocityX = 0f
                    velocityY = 0f
                } else {
                    // Rebote en el borde superior
                    ballY = ballRadius
                    velocityY = -velocityY * restitution
                }
            } else if (ballY + ballRadius > fieldHeight) {
                // Rebote en el borde inferior
                ballY = fieldHeight - ballRadius
                velocityY = -velocityY * restitution
            }

            // Dibuja la portería (rectángulo rojo arriba)
            val goalWidth = fieldWidth * 0.3f
            val goalHeight = 30f
            val goalLeftX = (fieldWidth - goalWidth) / 2f
            drawRect(
                color = Color.Red,
                topLeft = Offset(goalLeftX, 0f),
                size = Size(goalWidth, goalHeight)
            )

            // Dibuja la pelota (círculo blanco)
            drawCircle(
                color = Color.White,
                radius = ballRadius,
                center = Offset(ballX, ballY)
            )
        }

        // Mostrar marcador y datos de sensor
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Text(
                text = "Marcador: $score",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "accX: ${"%.2f".format(sensorValue.value.first)}  " +
                        "accY: ${"%.2f".format(sensorValue.value.second)}  " +
                        "accZ: ${"%.2f".format(sensorValue.value.third)}",
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "velX: ${"%.2f".format(velocityX)}  velY: ${"%.2f".format(velocityY)}",
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}
