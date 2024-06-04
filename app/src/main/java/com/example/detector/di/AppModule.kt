package com.example.detector.di

import com.example.detector.BuildConfig
import com.example.detector.data.GeminiRepository
import com.example.detector.data.GeminiRepositoryImpl
import com.example.detector.domain.usecase.GeminiUseCase
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindGeminiRepository(
        geminiRepositoryImpl: GeminiRepositoryImpl
    ): GeminiRepository

    @Binds
    @Singleton
    abstract fun bindGeminiUseCase(
        geminiRepository: GeminiRepository
    ): GeminiUseCase

    companion object {
        @Provides
        @Singleton
        fun provideGenerativeModel(): GenerativeModel {
            val apiKey = BuildConfig.API_KEY
            // Return an instance of GenerativeModel, make sure this is the correct way to initialize it
            return GenerativeModel.Builder()
                .apiKey(apiKey)
                .modelName("gemini-1.5-flash")
                .build()
        }
    }
}
