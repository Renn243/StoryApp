package com.raihanresa.storyapp

import com.raihanresa.storyapp.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..20) {
            val story = ListStoryItem(
                photoUrl = "https://static.zerochan.net/Diluc.Ragnvindr.full.3104029.jpg",
                createdAt = "2024-06-01T12:00:00",
                name = "Diluc",
                description = "Dawn, break forth!",
                lon = 123.456,
                id = "1",
                lat = 78.90
            )
            items.add(story)
        }
        return items
    }
}