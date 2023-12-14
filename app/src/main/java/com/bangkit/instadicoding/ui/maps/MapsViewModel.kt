package com.bangkit.instadicoding.ui.maps

import androidx.lifecycle.ViewModel
import com.bangkit.instadicoding.data.repository.AppRepository

class MapsViewModel (private val repository: AppRepository) : ViewModel() {
    
    fun getStoriesLocation(location: Int) = repository.getStoriesLocation(location)

}