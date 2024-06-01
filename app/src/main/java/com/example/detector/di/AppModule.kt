package com.example.detector.di

import com.example.detector.data.GeminiRepository
import com.example.detector.data.GeminiRepositoryImpl
import com.example.detector.domain.usecase.GeminiUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://your.api.base.url") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
