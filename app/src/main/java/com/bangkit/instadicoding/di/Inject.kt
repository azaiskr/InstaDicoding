package com.bangkit.instadicoding.di

import android.content.Context
import com.bangkit.instadicoding.data.database.StoriesDatabase
import com.bangkit.instadicoding.data.preference.UserPreferences
import com.bangkit.instadicoding.data.remote.api.ApiConfig
import com.bangkit.instadicoding.data.repository.AppRepository

object Inject {
    fun provideRepo(context: Context): AppRepository{
        val pref = UserPreferences.getInstance(context)
        val apiService = ApiConfig.getApiService(context)
        val database = StoriesDatabase.getDatabase(context)
        return AppRepository.getInstance(pref, apiService, database)
    }
}