package com.example.fundamentaltest1.data.remote

import com.example.fundamentaltest1.data.model.ResponseDetailUser
import com.example.fundamentaltest1.data.model.ResponseUser
import com.example.fundamentaltest1.data.model.items
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GithubService {
    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserGithub(): MutableList<items>

    @GET("users/{username}")
    suspend fun getDetailUserGithub(@Path("username") username: String): ResponseDetailUser

    @GET("users/{username}/followers")
    suspend fun getFollowersGithub(@Path("username") username: String): MutableList<items>

    @GET("users/{username}/following")
    suspend fun getFollowingsGithub(@Path("username") username: String): MutableList<items>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun searchUserGithub(@QueryMap params: Map<String, Any>): ResponseUser
}
