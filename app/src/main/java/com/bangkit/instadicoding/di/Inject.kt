package com.bangkit.instadicoding.di

import android.content.Context
import com.bangkit.instadicoding.data.database.StoriesDatabase
import com.bangkit.instadicoding.data.preference.UserPreferences
import com.bangkit.instadicoding.data.preference.dataStore
import com.bangkit.instadicoding.data.remote.api.ApiConfig
import com.bangkit.instadicoding.data.repository.AppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Inject {
    fun provideRepo(context: Context): AppRepository{
        val pref = UserPreferences.getInstance(context.dataStore)
        
        val user = runBlocking { pref.getUserPref().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoriesDatabase.getDatabase(context)
        return AppRepository.getInstance(pref, apiService, database)
    }
}