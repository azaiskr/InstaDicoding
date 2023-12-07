package com.bangkit.instadicoding.utils

sealed class State<out R> private constructor(){
    object Loading: State<Nothing>()
    data class Success<out T>(val data: T): State<T>()
    data class Error(val error: String?): State<Nothing>()
}
