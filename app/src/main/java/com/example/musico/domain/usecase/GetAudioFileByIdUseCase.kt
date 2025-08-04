package com.example.musico.domain.usecase

import com.example.musico.domain.model.AudioFile
import com.example.musico.domain.repository.MediaRepository
import javax.inject.Inject

class GetAudioFileByIdUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(id: Long): AudioFile? {
        return mediaRepository.getAudioFileById(id)
    }
} 