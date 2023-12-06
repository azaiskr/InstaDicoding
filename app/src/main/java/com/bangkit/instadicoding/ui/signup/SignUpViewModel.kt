package com.bangkit.instadicoding.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.instadicoding.data.remote.response.RegisterResponse
import com.bangkit.instadicoding.data.repository.AppRepository
import com.bangkit.instadicoding.utils.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repo: AppRepository,
) : ViewModel() {

    private val _registerState = MutableLiveData<State<RegisterResponse>>()
    val registerState: LiveData<State<RegisterResponse>> = _registerState

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            repo.register(name, email, password).collect(){
                _registerState.value = it
            }
        }
    }
}


