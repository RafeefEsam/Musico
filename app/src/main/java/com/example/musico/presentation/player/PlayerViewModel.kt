package com.example.musico.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musico.domain.model.AudioFile
import com.example.musico.domain.usecase.GetAudioFileByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getAudioFileByIdUseCase: GetAudioFileByIdUseCase
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

    private fun startPlayback(audioFile: AudioFile) {
        // TODO: Implement actual media player service communication
        _uiState.value = _uiState.value.copy(isPlaying = true)
    }

    private fun pausePlayback() {
        // TODO: Implement actual media player service communication
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }

    private fun resumePlayback() {
        // TODO: Implement actual media player service communication
        _uiState.value = _uiState.value.copy(isPlaying = true)
    }

    private fun stopPlayback() {
        // TODO: Implement actual media player service communication
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }
}

data class PlayerUiState(
    val audioFile: AudioFile? = null,
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val error: String? = null
) 