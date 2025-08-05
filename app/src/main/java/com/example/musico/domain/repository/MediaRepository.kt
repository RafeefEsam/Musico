package com.example.musico.domain.repository

import com.example.musico.domain.model.AudioFile
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getAudioFiles(): Flow<List<AudioFile>>
    suspend fun scanForAudioFiles()
    suspend fun getAudioFileById(id: Long): AudioFile?
    
    // Playback control
    suspend fun playAudio(audioFile: AudioFile)
    suspend fun pauseAudio()
    suspend fun resumeAudio()
    suspend fun playNextTrack()
    suspend fun playPreviousTrack()
    
    // Playback state
    fun getCurrentTrack(): Flow<AudioFile?>
    fun isPlaying(): Flow<Boolean>
    fun getCurrentPlaybackPosition(): Flow<Long>
    
    // Seeking
    suspend fun seekTo(position: Long)
} 