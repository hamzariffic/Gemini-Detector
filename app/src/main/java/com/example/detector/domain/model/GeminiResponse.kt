package com.example.detector.domain.model

// Data class representing the response from the Gemini API
data class GeminiResponse(
    val content: String, // The text content of the response will be mostly via text
    val images: List<String> = emptyList(), // List of image URLs (if any)
    val videoUrls: List<String> = emptyList(), //Handles video processing
    val audioUrls: List<String> = emptyList(),//Handles Audio processing
    val pdfUrls: List<String> = emptyList()//Handles PDFs
)