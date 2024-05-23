package com.raihanresa.storyapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raihanresa.storyapp.data.ResultState
import com.raihanresa.storyapp.data.remote.response.AddStoryResponse
import com.raihanresa.storyapp.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun uploadStory(file: MultipartBody.Part, description: RequestBody): LiveData<ResultState<AddStoryResponse>> = storyRepository.uploadImage(file, description)
}