package com.example.musico.domain.model

import android.graphics.Bitmap

data class AudioFile(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val filePath: String,
    val albumArtPath: String? = null,
    val size: Long = 0,
    val albumArt: Bitmap? = null
) 