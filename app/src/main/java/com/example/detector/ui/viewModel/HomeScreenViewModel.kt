package com.example.detector.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detector.domain.model.GeminiResponse
import com.example.detector.domain.usecase.GeminiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val geminiUseCase: GeminiUseCase) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _response = MutableStateFlow<GeminiResponse?>(null)
    val response: StateFlow<GeminiResponse?> = _response

    // State for the voice input
    val voiceInputEnabled: MutableState<Boolean> = mutableStateOf(false)

    // State for the text-to-speech toggle
    val textToSpeechEnabled: MutableState<Boolean> = mutableStateOf(false)

    // Function to handle search text input
    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    // Function to trigger AI interaction
    fun interactWithAI() {
        viewModelScope.launch {
            val prompt = buildPrompt(_searchText.value)
            val geminiResponse = geminiUseCase.invoke(prompt)
            _response.value = geminiResponse
        }
    }

    // Function to build the prompt for the Gemini model
    private fun buildPrompt(query: String): String {
        // Customize this prompt based on your medical domain
        return "I am a medical AI assistant. Please help me diagnose this medical condition: $query"
    }

    // Function to handle voice input
    fun onVoiceInputEnabledChange(isEnabled: Boolean) {
        voiceInputEnabled.value = isEnabled
    }

    // Function to handle text-to-speech toggle
    fun onTextToSpeechEnabledChange(isEnabled: Boolean) {
        textToSpeechEnabled.value = isEnabled
    }
}
