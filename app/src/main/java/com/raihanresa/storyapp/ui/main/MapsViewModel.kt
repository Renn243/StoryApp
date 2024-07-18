package com.raihanresa.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raihanresa.storyapp.data.ResultState
import com.raihanresa.storyapp.data.remote.response.StoryResponse
import com.raihanresa.storyapp.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation(): LiveData<ResultState<StoryResponse>> = storyRepository.getStoriesWithLocation()



}