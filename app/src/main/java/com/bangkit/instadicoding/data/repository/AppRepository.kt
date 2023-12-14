package com.bangkit.instadicoding.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.instadicoding.data.StoryRemoteMediator
import com.bangkit.instadicoding.data.database.StoriesDatabase
import com.bangkit.instadicoding.data.preference.UserPreferences
import com.bangkit.instadicoding.data.remote.api.ApiService
import com.bangkit.instadicoding.data.remote.response.ListStoryItem
import com.bangkit.instadicoding.data.remote.response.LoginResult
import com.bangkit.instadicoding.utils.State
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.Exception

class AppRepository private constructor(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService,
    private val database: StoriesDatabase,
) {

    //USER - login
    fun login(email: String, password: String) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.login(email, password)
            emit(State.Success(response.loginResult))
        } catch (e: Exception) {
            emit(State.Error(e.message))
        }
    }

    //USER - register
    fun register(name: String, email: String, password: String) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message))
        }
    }

    // USER - save pref
    suspend fun saveSession(user: LoginResult) {
        userPreferences.saveUserPref(user)
    }

    //USER- get pref
    fun getSession(): Flow<LoginResult> {
        return userPreferences.getUserPref()
    }

    //USER - clear pref
    suspend fun logout() {
        userPreferences.clearUserPref()
    }


    //STORY - get all data
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = { database.storiesDao().getStories() }
        ).liveData
    }

    //STORY - create post
    fun createPost(
        imageFile: File,
        description: String,
        latitude: Double?,
        longitude: Double?,
    ) = liveData {
        emit(State.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile,
        )

        try {
            val successResponse = apiService.createPost(multipartBody, requestBody, latitude, longitude)
            emit(State.Success(successResponse))
        } catch (e: Exception){
            emit(State.Error(e.message))
        }
    }

    //STORY - get with location
    fun getStoriesLocation(location: Int) = liveData {
        emit(State.Loading)
        try {
            val successResponse = apiService.getStoriesLocation(location)
            emit(State.Success(successResponse.listStory))
        } catch (e: Exception){
            emit(State.Error(e.message))
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null
        fun getInstance(
            userPreferences: UserPreferences,
            apiService: ApiService,
            database: StoriesDatabase,
        ): AppRepository =
            INSTANCE ?: synchronized(this) {
                AppRepository(
                    userPreferences,
                    apiService,
                    database
                ).also { INSTANCE = it }
            }
    }

}