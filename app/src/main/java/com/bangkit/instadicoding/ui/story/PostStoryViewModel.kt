package com.bangkit.instadicoding.ui.story

import androidx.lifecycle.ViewModel
import com.bangkit.instadicoding.data.repository.AppRepository
import java.io.File

class PostStoryViewModel(private val repository: AppRepository): ViewModel() {
    fun createPost(
        file: File,
        description: String,
        latitude: Double?,
        longitude: Double?,
    ) = repository.createPost(file, description, latitude, longitude)
}