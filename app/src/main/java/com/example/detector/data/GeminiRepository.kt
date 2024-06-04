package com.example.detector.data

import com.example.detector.domain.model.GeminiResponse
import com.example.detector.domain.usecase.GeminiUseCase
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.SafetySetting
import kotlinx.coroutines.flow.Flow

interface GeminiRepository : GeminiUseCase {
    override suspend fun generateContentStream(prompt: String): Flow<GeminiResponse>
    override suspend fun initializeModel(modelName: String, apiKey: String, generationConfig: GenerationConfig, safetySettings: List<SafetySetting>)
}
