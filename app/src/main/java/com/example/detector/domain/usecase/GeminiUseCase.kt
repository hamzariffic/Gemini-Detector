package com.example.detector.domain.usecase

import com.example.detector.domain.model.GeminiResponse
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.SafetySetting
import kotlinx.coroutines.flow.Flow

// Use case for interacting with the Gemini API
interface GeminiUseCase {
//    This function handles the asynchronous streaming of responses from the Gemini API. Streaming is necessary
    suspend fun generateContentStream(prompt: String): Flow<GeminiResponse>
//    Initializing Gemini model with necessary configurations.
    suspend fun initializeModel(modelName: String, apiKey: String, generationConfig: GenerationConfig, safetySettings: List<SafetySetting>)
}
