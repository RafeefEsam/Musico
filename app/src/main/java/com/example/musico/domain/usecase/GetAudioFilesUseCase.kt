package com.example.musico.domain.usecase

import com.example.musico.domain.model.AudioFile
import com.example.musico.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAudioFilesUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(): Flow<List<AudioFile>> {
        return mediaRepository.getAudioFiles()
    }
} 