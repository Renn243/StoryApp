package com.raihanresa.storyapp.ui.add

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.raihanresa.storyapp.data.ResultState
import com.raihanresa.storyapp.databinding.ActivityAddStoryBinding
import com.raihanresa.storyapp.ui.ViewModelFactory
import com.raihanresa.storyapp.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val addViewModel: AddViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        val descriptionText = binding.etDescription.text.toString()
        if (currentImageUri != null && descriptionText.isNotEmpty()) {
            val file = uriToFile(currentImageUri!!, this)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )
            val description = descriptionText.toRequestBody("text/plain".toMediaTypeOrNull())

            binding.progressIndicator.visibility = View.VISIBLE

            addViewModel.uploadStory(imageMultipart, description).observe(this) { resultState ->
                binding.progressIndicator.visibility = View.GONE

                when (resultState) {
                    is ResultState.Success -> {
                        Toast.makeText(this, "Story uploaded successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    is ResultState.Error -> {
                        Toast.makeText(this, "Failed to upload story: ${resultState.error}", Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Loading -> {
                       //
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please select an image and enter a description", Toast.LENGTH_SHORT).show()
        }
    }
}