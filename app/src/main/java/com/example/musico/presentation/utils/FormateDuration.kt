package com.example.musico.presentation.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}