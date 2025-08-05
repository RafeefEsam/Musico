package com.example.musico.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musico.domain.model.AudioFile
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.os.Handler
import android.os.Looper
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService : MediaSessionService() {
    
    @Inject
    lateinit var player: ExoPlayer
    
    private var mediaSession: MediaSession? = null
    
    private val _currentTrack = MutableStateFlow<AudioFile?>(null)
    val currentTrack: StateFlow<AudioFile?> = _currentTrack.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private var playlist: List<AudioFile> = emptyList()
    private var currentIndex: Int = -1
    private val handler = Handler(Looper.getMainLooper())
    private val positionUpdateInterval = 100L // Update every 100ms

    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            if (_isPlaying.value) {
                _currentPosition.value = player.currentPosition
                handler.postDelayed(this, positionUpdateInterval)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        
        initializePlayer()
        initializeMediaSession()
    }

    private fun initializePlayer() {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) {
                    handler.post(updatePositionRunnable)
                } else {
                    handler.removeCallbacks(updatePositionRunnable)
                }
            }
            
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                _currentPosition.value = newPosition.positionMs
                if (reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION ||
                    reason == Player.DISCONTINUITY_REASON_SEEK) {
                    updateCurrentTrackIfChanged()
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                updateCurrentTrackIfChanged()
            }
        })
    }

    private fun initializeMediaSession() {
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MediaSessionCallback())
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        handler.removeCallbacks(updatePositionRunnable)
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Media Playback",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    fun setPlaylist(files: List<AudioFile>, startIndex: Int = 0) {
        playlist = files
        currentIndex = startIndex.coerceIn(0, files.size - 1)
        
        player.clearMediaItems()
        files.forEach { audioFile ->
            val mediaItem = MediaItem.Builder()
                .setMediaId(audioFile.id.toString())
                .setUri(audioFile.filePath)
                .build()
            player.addMediaItem(mediaItem)
        }
        
        player.seekTo(currentIndex, 0)
        _currentTrack.value = files[currentIndex]
    }

    fun playAudio(audioFile: AudioFile) {
        val index = playlist.indexOfFirst { it.id == audioFile.id }
        if (index != -1) {
            currentIndex = index
            player.seekTo(index, 0)
            player.prepare()
            player.play()
            _currentTrack.value = audioFile
        }
    }

    fun pauseAudio() {
        player.pause()
    }

    fun resumeAudio() {
        player.play()
    }

    fun playNextTrack() {
        if (currentIndex < playlist.size - 1) {
            currentIndex++
            player.seekToNextMediaItem()
            _currentTrack.value = playlist[currentIndex]
        }
    }

    fun playPreviousTrack() {
        if (currentIndex > 0) {
            currentIndex--
            player.seekToPreviousMediaItem()
            _currentTrack.value = playlist[currentIndex]
        }
    }

    private inner class MediaSessionCallback : MediaSession.Callback {
        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: List<MediaItem>
        ): ListenableFuture<List<MediaItem>> {
            val updatedMediaItems = mediaItems.map { mediaItem ->
                mediaItem.buildUpon()
                    .setUri(mediaItem.mediaId)
                    .build()
            }
            return com.google.common.util.concurrent.Futures.immediateFuture(updatedMediaItems)
        }
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return binder
    }

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
        _currentPosition.value = position
    }

    private fun updateCurrentTrackIfChanged() {
        val newIndex = player.currentMediaItemIndex
        if (newIndex != currentIndex && newIndex in playlist.indices) {
            currentIndex = newIndex
            _currentTrack.value = playlist[newIndex]
        }
    }

    companion object {
        private const val CHANNEL_ID = "media_playback_channel"
    }
}


