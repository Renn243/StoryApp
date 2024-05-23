package com.raihanresa.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.raihanresa.storyapp.data.ResultState
import com.raihanresa.storyapp.data.remote.response.AddStoryResponse
import com.raihanresa.storyapp.data.remote.response.StoryResponse
import com.raihanresa.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {

    fun getStories(): LiveData<ResultState<StoryResponse>> = liveData(Dispatchers.IO) {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStories()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ResultState.Success(responseBody))
                } else {
                    emit(ResultState.Error("Response body is null"))
                }
            } else {
                emit(ResultState.Error(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Exception occurred"))
        }
    }

    fun uploadImage(file: MultipartBody.Part, description: RequestBody): LiveData<ResultState<AddStoryResponse>> = liveData(
        Dispatchers.IO) {
        try {
            val response = apiService.postStory(file, description)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ResultState.Success(responseBody))
                } else {
                    emit(ResultState.Error("Response body is null"))
                }
            } else {
                emit(ResultState.Error(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Exception occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}