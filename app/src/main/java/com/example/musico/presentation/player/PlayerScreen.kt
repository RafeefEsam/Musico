package com.example.musico.presentation.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.musico.R
import com.example.musico.presentation.utils.formatDuration
import com.example.musico.presentation.utils.getDefaultAlbumArtBitmap
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    navController: NavController,
    audioFileId: String,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(audioFileId) {
        viewModel.loadAudioFile(audioFileId.toLongOrNull() ?: 0L)
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A2E)) // Dark purple background
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    // Loading indicator with custom color
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color(0xFFE94560)
                    )
                }

                uiState.audioFile != null -> {
                    PlayerContent(
                        audioFile = uiState.audioFile!!,
                        isPlaying = uiState.isPlaying,
                        onPlayPause = { viewModel.togglePlayPause() },
                        onStop = { viewModel.stop() }
                    )
                }

                else -> {
                    Text(
                        "Audio file not found",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerContent(
    audioFile: com.example.musico.domain.model.AudioFile,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onStop: () -> Unit
) {
    val defaultBitmap = getDefaultAlbumArtBitmap(R.drawable.music_placeholder)

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val totalDuration = audioFile.duration.toFloat() // fallback: 2:35

    // Simulate playback progress (replace with ExoPlayer's actual position for real implementation)
    LaunchedEffect(isPlaying) {
        while (isPlaying && sliderPosition < totalDuration) {
            delay(1000)
            sliderPosition += 1f
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Album Art
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    bitmap = audioFile.albumArt?.asImageBitmap() ?: defaultBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1D1B29),
                                    Color(0x464527A0),
                                )
                            )
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Song Info
        Column(modifier = Modifier.align(Alignment.Start)) {
            Text(
                text = audioFile.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = audioFile.artist,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SeekBar with time
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = formatDuration(sliderPosition.toLong()),
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 8.dp)
            )

            Slider(
                value = sliderPosition.coerceIn(0f, totalDuration),
                onValueChange = { newValue -> sliderPosition = newValue },
                valueRange = 0f..totalDuration,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF7B57E4),
                    activeTrackColor = Color(0xFF7B57E4),
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )

            Text(
                text = formatDuration(totalDuration.toLong()),
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Playback Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* TODO: Implement previous */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = "Previous",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFFBAA8ED)
                )
            }

            IconButton(
                onClick = onPlayPause,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.pause else R.drawable.play
                    ),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(50.dp),
                    tint = Color(0xFFBAA8ED)
                )
            }

            IconButton(
                onClick = { /* TODO: Implement next */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Next",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFFBAA8ED)
                )
            }
        }
    }
}