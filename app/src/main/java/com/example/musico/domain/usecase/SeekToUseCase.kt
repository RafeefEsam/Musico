package com.example.musico.domain.usecase

import com.example.musico.domain.repository.MediaRepository
import javax.inject.Inject

class SeekToUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(position: Long) {
        mediaRepository.seekTo(position)
    }
}