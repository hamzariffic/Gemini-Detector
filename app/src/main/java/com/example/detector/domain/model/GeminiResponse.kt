package com.example.detector.domain.model

// Data class representing the response from the Gemini API
data class GeminiResponse(
    val content: String, // The text content of the response
    val images: List<String> = emptyList() // List of image URLs (if any)
)