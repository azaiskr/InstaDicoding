package com.bangkit.instadicoding.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.instadicoding.data.remote.response.LoginResult
import com.bangkit.instadicoding.data.repository.AppRepository
import com.bangkit.instadicoding.utils.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository):ViewModel() {

    private val _loginResult = MutableLiveData<State<LoginResult>>()
    val loginResult: LiveData<State<LoginResult>> = _loginResult

    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(email, password).collect(){
                _loginResult.value = it
            }
        }
    }

    fun saveSession(user: LoginResult){
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}