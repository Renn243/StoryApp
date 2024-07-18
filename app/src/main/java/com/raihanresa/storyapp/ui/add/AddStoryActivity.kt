package com.raihanresa.storyapp.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        binding.includeLocationCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLastLocation()
            } else {
                // Clear location fields or handle accordingly
            }
        }
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

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude

                    Log.d(TAG, "Latitude: $latitude, Longitude: $longitude")
                } else {
                    Log.d(TAG, "Location is null")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting location", e)
            }
    }
    private fun uploadImage() {
        val descriptionText = binding.descriptionEditText.text.toString()

        if (currentImageUri != null && descriptionText.isNotEmpty()) {
            val file = uriToFile(currentImageUri!!, this).reduceFileImage()
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )
            val description = descriptionText.toRequestBody("text/plain".toMediaTypeOrNull())

            binding.progressIndicator.visibility = View.VISIBLE

            val uploadFunction = if (binding.includeLocationCheckBox.isChecked) {
                val lat = latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val lon = longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                addViewModel.uploadStoryWithLocation(imageMultipart, description, lat, lon)
            } else {
                addViewModel.uploadStory(imageMultipart, description)
            }

            uploadFunction.observe(this) { resultState ->
                when (resultState) {
                    is ResultState.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(this, "Story uploaded successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    is ResultState.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(this, "Failed to upload story: ${resultState.error}", Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        } else {
            Toast.makeText(this, "Please select an image and enter a description", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "AddStoryActivity"
        private const val REQUEST_LOCATION_PERMISSION = 100
    }
}