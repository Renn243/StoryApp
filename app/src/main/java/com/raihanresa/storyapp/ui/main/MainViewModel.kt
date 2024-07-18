package com.raihanresa.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.raihanresa.storyapp.data.remote.response.ListStoryItem
import com.raihanresa.storyapp.repository.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories(): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getPagedStories().cachedIn(viewModelScope)
}