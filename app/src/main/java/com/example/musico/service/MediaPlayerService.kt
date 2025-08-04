package com.example.musico.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.musico.domain.model.AudioFile
import javax.inject.Inject

class MediaPlayerService : Service() {

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    private val binder = MediaPlayerBinder()

    inner class MediaPlayerBinder : Binder() {
        fun getService(): MediaPlayerService = this@MediaPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize MediaPlayer
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun playAudio(audioFile: AudioFile) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioFile.filePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    fun resume() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
} 