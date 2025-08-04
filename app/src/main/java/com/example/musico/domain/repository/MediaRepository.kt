package com.example.musico.domain.repository

import com.example.musico.domain.model.AudioFile
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getAudioFiles(): Flow<List<AudioFile>>
    suspend fun scanForAudioFiles()
    suspend fun getAudioFileById(id: Long): AudioFile?
} 