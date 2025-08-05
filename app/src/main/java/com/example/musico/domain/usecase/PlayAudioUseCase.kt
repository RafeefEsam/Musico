package com.example.musico.domain.usecase

import com.example.musico.domain.model.AudioFile
import com.example.musico.domain.repository.MediaRepository
import javax.inject.Inject

class PlayAudioUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(audioFile: AudioFile) {
        mediaRepository.playAudio(audioFile)
    }
}