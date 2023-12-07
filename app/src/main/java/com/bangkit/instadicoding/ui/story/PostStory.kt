package com.bangkit.instadicoding.ui.story

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.databinding.ActivityPostStoryBinding
import com.bangkit.instadicoding.ui.main.MainActivity
import com.bangkit.instadicoding.utils.State
import com.bangkit.instadicoding.utils.ViewModelFactory
import com.bangkit.instadicoding.utils.getImageUri
import com.bangkit.instadicoding.utils.reduceFileSize
import com.bangkit.instadicoding.utils.uriToFile

class PostStory : AppCompatActivity() {


    private lateinit var binding: ActivityPostStoryBinding
    private var imageUri: Uri? = null
    private val viewModel by viewModels<PostStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_story)

        setUpButton()
    }

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

            viewModel.createPost(imageFile, description).observe(this) {response ->
                if (response != null) {
                    when(response){
                        is State.Loading -> {
                            showLoading(true)
                        }
                        is State.Success -> {
                            showToast(response.data.message)
                            showLoading(false)
                            // double check
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}