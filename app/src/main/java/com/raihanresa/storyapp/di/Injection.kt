package com.raihanresa.storyapp.di

import android.content.Context
import com.raihanresa.storyapp.data.local.UserPreference
import com.raihanresa.storyapp.data.remote.retrofit.ApiConfig
import com.raihanresa.storyapp.repository.StoryRepository
import com.raihanresa.storyapp.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context)
        val tokenFlow = userPreference.tokenFlow
        val apiService = ApiConfig.getApiService(tokenFlow)
        return UserRepository.getInstance(apiService, userPreference)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val userPreference = UserPreference.getInstance(context)
        val tokenFlow = userPreference.tokenFlow
        val apiService = ApiConfig.getApiService(tokenFlow)
        return StoryRepository.getInstance(apiService)
    }
}