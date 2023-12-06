package com.bangkit.instadicoding.data.repository

import androidx.lifecycle.LiveData
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
import com.bangkit.instadicoding.data.remote.response.RegisterResponse
import com.bangkit.instadicoding.utils.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepository private constructor(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService,
    private val database: StoriesDatabase,
) {

    //USER - login
    suspend fun login(email: String, password: String): Flow<State<LoginResult>> = flow {
        try {
            emit(State.Loading)
            val response = apiService.login(email, password)
            emit(State.Success(response.loginResult))
        } catch (e: Exception) {
            emit(State.Error(e))
        }
    }

    //USER - register
    suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<State<RegisterResponse>> = flow {
        try {
            emit(State.Loading)
            val response = apiService.register(name, email, password)
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e))
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