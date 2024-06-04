package com.example.detector.data

import com.example.detector.domain.model.GeminiResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Part
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.SafetySetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : GeminiRepository {

    // Function to initialize the generative model with configurations and safety settings
    override suspend fun initializeModel(
        modelName: String,
        apiKey: String,
        generationConfig: GenerationConfig,
        safetySettings: List<SafetySetting>
    ) {
        withContext(Dispatchers.IO) {
            generativeModel.initialize(
                modelName = modelName,
                apiKey = apiKey,
                generationConfig = generationConfig,
                safetySettings = safetySettings
            )
        }
    }

    // Function to handle asynchronous streaming of responses from the Gemini API
    override suspend fun generateContentStream(prompt: String): Flow<GeminiResponse> = flow {
        try {
            val responseStream = generativeModel.generateContentStream(
                listOf(Part.fromText(prompt))
            )
            responseStream.collect { chunk ->
                val textContent = chunk.parts.filter { it.mimeType.startsWith("text/") }
                    .joinToString("\n") { it.textContent }

                val imageUrls = chunk.parts.filter { it.mimeType.startsWith("image/") }
                    .map { it.uri }
                val videoUrls = chunk.parts.filter { it.mimeType.startsWith("video/") }
                    .map { it.uri }
                val audioUrls = chunk.parts.filter { it.mimeType.startsWith("audio/") }
                    .map { it.uri }
                val pdfUrls = chunk.parts.filter { it.mimeType.startsWith("application/pdf") }
                    .map { it.uri }

                emit(
                    GeminiResponse(
                        content = textContent,
                        images = imageUrls,
                        videoUrls = videoUrls,
                        audioUrls = audioUrls,
                        pdfUrls = pdfUrls
                    )
                )
            }
        } catch (e: Exception) {
            emit(GeminiResponse(content = "Error: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}
