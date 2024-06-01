package com.example.detector.data

import com.example.detector.domain.model.GeminiResponse
import com.example.detector.domain.usecase.GeminiUseCase

interface GeminiRepository : GeminiUseCase {
    override suspend fun invoke(prompt: String): GeminiResponse
}
