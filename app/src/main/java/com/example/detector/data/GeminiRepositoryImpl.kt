package com.example.detector.data

import com.example.detector.domain.model.GeminiResponse
import com.example.detector.domain.usecase.GeminiUseCase
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : GeminiRepository, GeminiUseCase {

    override suspend fun invoke(prompt: String, additionalInputs: List<Part>? = null): GeminiResponse {
        return withContext(Dispatchers.IO) {
            try {
                // Prepare the request parts, including the prompt and any additional inputs
                val requestParts = mutableListOf<Part>(Part.fromText(prompt))
                additionalInputs?.let { requestParts.addAll(it) }

                // Generate content using the multimodal generative model
                val result = generativeModel.generateContent(requestParts)

                // Assuming 'generateContent' returns a response with text
                GeminiResponse(content = result.text)
            } catch (e: Exception) {
                GeminiResponse(content = "Error: ${e.message}")
            }
        }
    }
}
