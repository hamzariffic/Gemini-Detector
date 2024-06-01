package com.example.detector.domain.usecase

import com.example.detector.domain.model.GeminiResponse

interface GeminiUseCase {
    suspend fun invoke(prompt: String): GeminiResponse
}

