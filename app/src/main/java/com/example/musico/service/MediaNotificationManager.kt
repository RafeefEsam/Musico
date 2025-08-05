package com.example.musico.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.example.musico.R
import androidx.core.net.toUri

class MediaNotificationManager(
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val mediaSession: MediaSession
) {
    private var currentNotification: Notification? = null

    @UnstableApi
    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            notificationManager.cancel(NOTIFICATION_ID)
            if (dismissedByUser) {
                // Stop playback when user dismisses notification
                mediaSession.player.pause()
            }
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing) {
                // Keep service in foreground with notification
                (context as? MediaPlayerService)?.startForeground(notificationId, notification)
            } else {
                // Move service to background but keep notification
                (context as? MediaPlayerService)?.apply {
                    stopForeground(false)
                    notificationManager.notify(notificationId, notification)
                }
            }
            currentNotification = notification
        }
    }

    @UnstableApi
    private val playerNotificationManager = PlayerNotificationManager.Builder(
        context,
        NOTIFICATION_ID,
        CHANNEL_ID
    ).apply {
        setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return (context as? MediaPlayerService)?.currentTrack?.value?.title ?: "Unknown"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val service = context as? MediaPlayerService
                val currentTrackId = service?.currentTrack?.value?.id?.toString() ?: "0"
                val currentPosition = service?.currentPosition?.value ?: 0L
                
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "musico://player?id=$currentTrackId&position=$currentPosition".toUri()
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                
                return PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    getPendingIntentFlags()
                )
            }

            override fun getCurrentContentText(player: Player): CharSequence {
                return (context as? MediaPlayerService)?.currentTrack?.value?.artist ?: "Unknown Artist"
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                return (context as? MediaPlayerService)?.currentTrack?.value?.albumArt
            }
        })
        setNotificationListener(notificationListener)
        setChannelNameResourceId(R.string.media_notification_channel)
        setChannelDescriptionResourceId(R.string.media_notification_channel_description)
    }.build()

    @UnstableApi
    fun showNotification(player: Player) {
        playerNotificationManager.setPlayer(player)
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_LOW)
        playerNotificationManager.setUseStopAction(false)
        playerNotificationManager.setUseNextActionInCompactView(true)
        playerNotificationManager.setUsePreviousActionInCompactView(true)
        playerNotificationManager.setMediaSessionToken(mediaSession.platformToken)
    }

    @UnstableApi
    fun hideNotification() {
        playerNotificationManager.setPlayer(null)
        notificationManager.cancel(NOTIFICATION_ID)
        (context as? MediaPlayerService)?.stopForeground(true)
    }

    private fun getPendingIntentFlags(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "media_playback_channel"
    }
}