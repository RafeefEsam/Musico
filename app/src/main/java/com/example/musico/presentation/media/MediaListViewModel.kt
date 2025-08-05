package com.example.musico.presentation.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musico.domain.model.AudioFile
import com.example.musico.domain.usecase.GetAudioFilesUseCase
import com.example.musico.domain.usecase.ScanAudioFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val getAudioFilesUseCase: GetAudioFilesUseCase,
    private val scanAudioFilesUseCase: ScanAudioFilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaListUiState())
    val uiState: StateFlow<MediaListUiState> = _uiState.asStateFlow()

    fun loadAudioFiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                scanAudioFilesUseCase()
                
                getAudioFilesUseCase()
                    .catch { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                    .collect { audioFiles ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            audioFiles = audioFiles,
                            error = null
                        )
                    }
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun onAudioFileClick(audioFile: AudioFile) {
        _uiState.value = _uiState.value.copy(selectedAudioFile = audioFile)
    }

}

data class MediaListUiState(
    val audioFiles: List<AudioFile> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedAudioFile: AudioFile? = null
) 