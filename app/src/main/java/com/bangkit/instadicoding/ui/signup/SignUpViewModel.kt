package com.bangkit.instadicoding.ui.signup

import androidx.lifecycle.ViewModel
import com.bangkit.instadicoding.data.repository.AppRepository

class SignUpViewModel(
    private val repo: AppRepository,
) : ViewModel() {
    fun register(name: String, email: String, password: String) = repo.register(name, email,password)
}


