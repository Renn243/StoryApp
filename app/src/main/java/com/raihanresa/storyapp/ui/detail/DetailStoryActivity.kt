package com.raihanresa.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.raihanresa.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO_URL = "extra_photo_url"
        const val EXTRA_DESCRIPTION = "extra_description"
    }

    private lateinit var binding: ActivityDetailStoryBinding
    private var storyName: String? = null
    private var storyPhotoUrl: String? = null
    private var storyDescription: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyName = intent.getStringExtra(EXTRA_NAME)
        storyPhotoUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        storyDescription = intent.getStringExtra(EXTRA_DESCRIPTION)

        storyName?.let { name ->
            Glide.with(this)
                .load(storyPhotoUrl)
                .into(binding.imgStory)

            binding.tvName.text = name
            binding.tvDescription.text = storyDescription
        }
    }
}