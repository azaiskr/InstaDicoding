package com.bangkit.instadicoding.ui.story

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.databinding.ActivityPostStoryBinding
import com.bangkit.instadicoding.utils.getImageUri

class PostStory : AppCompatActivity() {

    private lateinit var binding: ActivityPostStoryBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_story)

        setUpButton()

//        binding.btnGalery.setOnClickListener {
//            startGallery()
//        }
//
//        binding.btnCamera.setOnClickListener {
//            startCamera()
//        }
//
//        binding.btnUpload.setOnClickListener {
//            Toast.makeText(this, "Upload Story", Toast.LENGTH_SHORT).show()
//        }

    }

    private fun setUpButton(){
        with(binding){
            btnGalery.setOnClickListener {
                startGallery()
            }
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnUpload.setOnClickListener {

            }
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            showImage()
        } else {
            Log.d("PostStory", "No media selected")
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera(){
        imageUri = getImageUri(this)
        launcherCamera.launch(imageUri)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){ isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        imageUri?.let{
            binding.ivContent.setImageURI(it)
            Log.d("PostStory", "Show Image: $it")
        }
    }
}