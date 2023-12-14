package com.bangkit.instadicoding.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.instadicoding.data.repository.AppRepository
import com.bangkit.instadicoding.di.Inject
import com.bangkit.instadicoding.ui.login.LoginViewModel
import com.bangkit.instadicoding.ui.main.MainViewModel
import com.bangkit.instadicoding.ui.maps.MapsViewModel
import com.bangkit.instadicoding.ui.signup.SignUpViewModel
import com.bangkit.instadicoding.ui.story.PostStoryViewModel

class ViewModelFactory(private val repository: AppRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PostStoryViewModel::class.java) -> {
                PostStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory{
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class) {
                    INSTANCE = ViewModelFactory(
                        Inject.provideRepo(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}