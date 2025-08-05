package com.example.musico.domain.usecase

import com.example.musico.domain.repository.MediaRepository
import javax.inject.Inject

class PlayPreviousTrackUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke() {
        mediaRepository.playPreviousTrack()
    }
}