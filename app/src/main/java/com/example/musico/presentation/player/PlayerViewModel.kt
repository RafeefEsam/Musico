package com.example.musico.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musico.domain.model.AudioFile
import com.example.musico.domain.repository.MediaRepository
import com.example.musico.domain.usecase.GetAudioFileByIdUseCase
import com.example.musico.domain.usecase.PauseAudioUseCase
import com.example.musico.domain.usecase.PlayAudioUseCase
import com.example.musico.domain.usecase.PlayNextTrackUseCase
import com.example.musico.domain.usecase.PlayPreviousTrackUseCase
import com.example.musico.domain.usecase.ResumeAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getAudioFileByIdUseCase: GetAudioFileByIdUseCase,
    private val mediaRepository: MediaRepository,
    private val playAudioUseCase: PlayAudioUseCase,
    private val pauseAudioUseCase: PauseAudioUseCase,
    private val resumeAudioUseCase: ResumeAudioUseCase,
    private val playNextTrackUseCase: PlayNextTrackUseCase,
    private val playPreviousTrackUseCase: PlayPreviousTrackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadAudioFile(audioFileId: Long) {
        if (audioFileId == 0L) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val audioFile = getAudioFileByIdUseCase(audioFileId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    audioFile = audioFile
                )
                
                // Start playing the audio file
                if (audioFile != null) {
                    startPlayback(audioFile)
                }
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to load audio file"
                )
            }
        }
    }

    fun togglePlayPause() {
        val currentState = _uiState.value
        if (currentState.isPlaying) {
            pausePlayback()
        } else {
            resumePlayback()
        }
    }

    fun stop() {
        stopPlayback()
    }

    init {
        viewModelScope.launch {
            mediaRepository.getCurrentTrack().collect { track ->
                _uiState.value = _uiState.value.copy(audioFile = track)
            }
        }
        
        viewModelScope.launch {
            mediaRepository.isPlaying().collect { isPlaying ->
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
            }
        }
        
        viewModelScope.launch {
            mediaRepository.getCurrentPlaybackPosition().collect { position ->
                _uiState.value = _uiState.value.copy(currentPosition = position)
            }
        }
    }

    private fun startPlayback(audioFile: AudioFile) {
        viewModelScope.launch {
            playAudioUseCase(audioFile)
        }
    }

    private fun pausePlayback() {
        viewModelScope.launch {
            pauseAudioUseCase()
        }
    }

    private fun resumePlayback() {
        viewModelScope.launch {
            resumeAudioUseCase()
        }
    }

    private fun stopPlayback() {
        viewModelScope.launch {
            pauseAudioUseCase()
        }
    }

    fun playNext() {
        viewModelScope.launch {
            playNextTrackUseCase()
        }
    }

    fun playPrevious() {
        viewModelScope.launch {
            playPreviousTrackUseCase()
        }
    }
}

data class PlayerUiState(
    val audioFile: AudioFile? = null,
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val error: String? = null,
    val currentPosition: Long = 0L
) 