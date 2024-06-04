package com.example.detector.ui.viewModel

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.detector.BuildConfig
import com.example.detector.domain.model.GeminiResponse
import com.example.detector.domain.usecase.GeminiUseCase
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for the HomeScreen
@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val geminiUseCase: GeminiUseCase) : ViewModel() {

    // Start the state for the text input and response both which are immutable onSubmit()
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _response = MutableStateFlow<GeminiResponse?>(null)
    val response: StateFlow<GeminiResponse?> = _response

    // State for the voice input from the user
    val voiceInputEnabled: MutableState<Boolean> = mutableStateOf(false)

    // State for the text-to-speech toggle to allow the user to enable or disable it
    val textToSpeechEnabled: MutableState<Boolean> = mutableStateOf(false)

    // Function to handle search text input
    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    // Function to trigger AI interaction
    fun interactWithAI(textInput: String?, imageInput: List<Bitmap>?, voiceInput: String?) {
        viewModelScope.launch {
            val prompt = buildPrompt(textInput, imageInput, voiceInput)
            // Collect the stream of responses from the Gemini model. We can achieve faster interactions by not waiting for Gemini to complete the entire result,
            // and instead use streaming to handle partial results.
            geminiUseCase.generateContentStream(prompt).collect { chunk ->
                _response.value = chunk
            }
        }
    }

    // Function to build the prompt for the Gemini model so that it aids in medical information
    private fun buildPrompt(textInput: String?, imageInput: List<Bitmap>?, voiceInput: String?): String {
        // Customize this prompt based on a specific medical domain. For Kremlin we'll just prompt the user to input their symptoms
        // BUT all the three inputs are optional (text input, voice input, image input) so we need to handle that.
        // If there's no text input, we'll just use the voice input. If there's no voice input, we'll just use the image input
        // The return string is a concatenation of the base prompt, text input, voice input, and image input
        val basePrompt = "I am a medical AI assistant. Please provide information about your symptoms or medical condition."
        val textPrompt = textInput ?: ""
        val voicePrompt = voiceInput ?: ""
        val imagePrompt = imageInput?.joinToString(separator = " ") { "Image included" } ?: ""

        return "$basePrompt $textPrompt $voicePrompt $imagePrompt"
    }

    // Function to handle voice input from the user
    fun onVoiceInputEnabledChange(isEnabled: Boolean) {
        voiceInputEnabled.value = isEnabled
    }

    // Function to handle text-to-speech toggle
    fun onTextToSpeechEnabledChange(isEnabled: Boolean) {
        textToSpeechEnabled.value = isEnabled
    }

    // Initialize the generative model with appropriate parameters and safety settings when the view model is created
    // These have also been parsed as constructors in the GeminiUseCase interface class
    init {
        // Initialize generative model with appropriate parameters and safety settings
        viewModelScope.launch {
            val generationConfig = GenerationConfigBuilder()
                .temperature(0.7f)
                .topK(20)
                .topP(0.9f)
                .maxOutputTokens(200)
                .stopSequences(listOf("Please consult a medical professional.", "Thank you", "That was helpful."))
                .build()

            geminiUseCase.initializeModel(
                modelName = "gemini-1.5-flash",
                apiKey = BuildConfig.API_KEY,
                generationConfig = generationConfig,
                safetySettings = listOf(
                    SafetySetting.newBuilder()
                        .setHarmCategory(HarmCategory.HARASSMENT)
                        .setBlockThreshold(BlockThreshold.ONLY_HIGH)
                        .build(),
                    SafetySetting.newBuilder()
                        .setHarmCategory(HarmCategory.HATE_SPEECH)
                        .setBlockThreshold(BlockThreshold.MEDIUM_AND_ABOVE)
                        .build()
                )
            )
        }
    }
}


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val geminiUseCase: GeminiUseCase) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _response = MutableStateFlow<GeminiResponse?>(null)
    val response: StateFlow<GeminiResponse?> = _response

    private val voiceInputEnabled: MutableState<Boolean> = mutableStateOf(false)
    private val textToSpeechEnabled: MutableState<Boolean> = mutableStateOf(false)

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    fun interactWithAI(textInput: String?, imageInput: List<Bitmap>?, voiceInput: String?) {
        viewModelScope.launch {
            val prompt = buildPrompt(textInput, imageInput, voiceInput)
            geminiUseCase.generateContentStream(prompt).collect { chunk ->
                _response.value = chunk
            }
        }
    }

    private fun buildPrompt(textInput: String?, imageInput: List<Bitmap>?, voiceInput: String?): String {
        val basePrompt = "I am a medical AI assistant. Please provide information about your symptoms or medical condition."
        val textPrompt = textInput ?: ""
        val voicePrompt = voiceInput ?: ""
        val imagePrompt = imageInput?.joinToString(separator = " ") { "Image included" } ?: ""

        return "$basePrompt $textPrompt $voicePrompt $imagePrompt"
    }

    fun onVoiceInputEnabledChange(isEnabled: Boolean) {
        voiceInputEnabled.value = isEnabled
    }

    fun onTextToSpeechEnabledChange(isEnabled: Boolean) {
        textToSpeechEnabled.value = isEnabled
    }

    init {
        viewModelScope.launch {
            val generationConfig = GenerationConfigBuilder()
                .temperature(0.7f)
                .topK(20)
                .topP(0.9f)
                .maxOutputTokens(200)
                .stopSequences(listOf("Please consult a medical professional.", "Thank you", "That was helpful."))
                .build()

            geminiUseCase.initializeModel(
                modelName = "gemini-1.5-flash",
                apiKey = BuildConfig.API_KEY,
                generationConfig = generationConfig,
                safetySettings = listOf(
                    SafetySetting.newBuilder()
                        .setHarmCategory(HarmCategory.HARASSMENT)
                        .setBlockThreshold(BlockThreshold.ONLY_HIGH)
                        .build(),
                    SafetySetting.newBuilder()
                        .setHarmCategory(HarmCategory.HATE_SPEECH)
                        .setBlockThreshold(BlockThreshold.MEDIUM_AND_ABOVE)
                        .build()
                )
            )
        }
    }
}
