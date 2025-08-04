package com.example.musico.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import com.example.musico.domain.model.AudioFile
import com.example.musico.domain.repository.MediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MediaRepository {

    private val _audioFiles = MutableStateFlow<List<AudioFile>>(emptyList())
    private val audioFiles: Flow<List<AudioFile>> = _audioFiles.asStateFlow()

    override fun getAudioFiles(): Flow<List<AudioFile>> = audioFiles

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun scanForAudioFiles() {
        val files = mutableListOf<AudioFile>()
        val contentResolver: ContentResolver = context.contentResolver

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        contentResolver.query(
            uri,
            null,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)?.takeIf { it.isNotBlank() } ?: "Unknown Title"
                val artist = cursor.getString(artistColumn)?.takeIf { it.isNotBlank() } ?: "Unknown Artist"
                val album = cursor.getString(albumColumn)?.takeIf { it.isNotBlank() } ?: "Unknown Album"
                val duration = cursor.getLong(durationColumn)
                val filePath = cursor.getString(dataColumn) ?: ""
                val size = cursor.getLong(sizeColumn)
                val albumId = cursor.getLong(albumIdColumn)

                // ✅ Load album art thumbnail safely
                val albumUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )

                var albumArt: Bitmap?
                try {
                    albumArt = contentResolver.loadThumbnail(
                        albumUri,
                        Size(200, 200), // You can change size
                        null
                    )
                } catch (_: Exception) {
                    // Album art might be missing or inaccessible — ignore
                    albumArt = null
                }

                if (filePath.isNotBlank()) {
                    val audioFile = AudioFile(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        filePath = filePath,
                        size = size,
                        albumArt = albumArt
                    )
                    files.add(audioFile)
                }
            }
        }

        _audioFiles.value = files
    }

    override suspend fun getAudioFileById(id: Long): AudioFile? {
        return _audioFiles.value.find { it.id == id }
    }
}
