package com.bangkit.instadicoding.utils

sealed class State<out T> private constructor(){
    data object Loading: State<Nothing>()
    data class Success<out T>(val data: T): State<T>()
    data class Error(val error: Throwable): State<Nothing>()
}
