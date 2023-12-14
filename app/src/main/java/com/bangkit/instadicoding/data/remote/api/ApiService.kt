package com.bangkit.instadicoding.data.remote.api

import com.bangkit.instadicoding.data.remote.response.LoginResponse
import com.bangkit.instadicoding.data.remote.response.RegisterResponse
import com.bangkit.instadicoding.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun createPost(
        @Part file: MultipartBody.Part,
        @Part("description") description : RequestBody,
        @Part("lat") lat : Double?,
        @Part("lon") lon : Double?,
    ): RegisterResponse

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location: Int,
    ): StoriesResponse
}