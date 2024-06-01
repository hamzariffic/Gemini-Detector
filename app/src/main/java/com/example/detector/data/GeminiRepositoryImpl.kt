package com.example.detector.data

import com.example.detector.domain.model.GeminiResponse
import com.example.detector.domain.usecase.GeminiUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    retrofit: Retrofit
) : GeminiUseCase {

    private interface GeminiApi {
        @POST("/gemini/v1/generate")
        suspend fun generateText(@Body prompt: String): GeminiResponse
    }

    private val api: GeminiApi = retrofit.create(GeminiApi::class.java)

    override suspend fun invoke(prompt: String): GeminiResponse {
        return withContext(Dispatchers.IO) {
            try {
                api.generateText(prompt)
            } catch (e: Exception) {
                GeminiResponse(content = "Error: ${e.message}")
            }
        }
    }
}
