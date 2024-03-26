package com.example.fundamentaltest1.data.remote

import android.content.ClipData.Item
import com.example.fundamentaltest1.data.model.ResponseDetailUser
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.data.model.ResponseUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GithubService {
    @GET("users")
    suspend fun getUserGithub(): MutableList<items>

    @GET("users/{username}")
    suspend fun getDetailUserGithub(@Path("username") username:String): ResponseDetailUser
}
