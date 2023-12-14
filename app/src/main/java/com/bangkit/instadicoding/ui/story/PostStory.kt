package com.bangkit.instadicoding.ui.story

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.databinding.ActivityPostStoryBinding
import com.bangkit.instadicoding.ui.main.MainActivity
import com.bangkit.instadicoding.utils.State
import com.bangkit.instadicoding.utils.ViewModelFactory
import com.bangkit.instadicoding.utils.getImageUri
import com.bangkit.instadicoding.utils.reduceFileSize
import com.bangkit.instadicoding.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class PostStory : AppCompatActivity() {


    private lateinit var binding: ActivityPostStoryBinding
    private var imageUri: Uri? = null
    private val viewModel by viewModels<PostStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.add_story)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.rounded_arrow_back_ios_24)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setUpButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun setUpButton() {
        with(binding) {
            btnGalery.setOnClickListener {
                startGallery()
            }
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnUpload.setOnClickListener {
                createPost()
            }
            locationSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    requestLocationUpdates()
                    Toast.makeText(this@PostStory, "Location on", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PostStory, "Location off", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun requestLocationUpdates()  {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                        binding.locationSwitch.isChecked = true

                        Log.d("PostStory", "Latitude: $latitude, Longitude: $longitude")
                    }
                }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    latitude = null
                    longitude = null
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                    binding.locationSwitch.isChecked = false
                }
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
            imageUri = uri
            showImage()
        } else {
            Log.d("PostStory", "No media selected")
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        imageUri = getImageUri(this)
        launcherCamera.launch(imageUri)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        imageUri?.let {
            binding.ivContent.setImageURI(it)
            Log.d("PostStory", "Show Image: $it")
        }
    }

    private fun createPost() {
        imageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileSize()
            Log.d("PostStory", "Create Post: $imageFile")
            val description = binding.edPostDesc.text.toString()

            viewModel.createPost(imageFile, description, latitude, longitude).observe(this) { response ->
                if (response != null) {
                    when (response) {
                        is State.Loading -> {
                            showLoading(true)
                        }

                        is State.Success -> {
                            showToast(response.data.message)
                            showLoading(false)
                            // double check
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
//                            finish()
                        }

                        is State.Error -> {
                            showToast(response.error)
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.please_select_an_image))
    }

    private fun showToast(mssg: String?) {
        Toast.makeText(this, mssg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 101
    }

}