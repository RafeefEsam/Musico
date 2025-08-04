package com.example.musico.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularGlowBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        // Base dark background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1D1B29))
        )

        // Top-left circular glow
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-60).dp, y = (-40).dp) // Move to top-left
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x334527A0),
                            Color(0xFF1D1B29)
                        ),
                        radius = 300.dp.value
                    ),
                    shape = CircleShape
                )
        )


        Box(modifier = Modifier.fillMaxSize()) {
            // Center circular glow
            Box(
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.Center) // Center in the parent Box
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x334527A0), // Center
                                Color(0xFF1D1B29)  // Edge
                            ),
                            radius = 370.dp.value // Half the size to match the box
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Bottom-right circular glow
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(
                    x = 190.dp,  // Move right
                    y = 500.dp   // Move down
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x334527A0), // Center
                            Color(0xFF1D1B29)  // Edge
                        ),
                        radius = 300.dp.value
                    ),
                    shape = CircleShape
                )
        )

        // Content on top
        content()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun CircularGlowBackgroundPreview() {
    CircularGlowBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sample Content",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}