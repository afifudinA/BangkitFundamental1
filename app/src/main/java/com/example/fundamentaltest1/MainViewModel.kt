package com.example.fundamentaltest1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundamentaltest1.data.remote.ApiClient
import com.example.fundamentaltest1.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    val resultUser = MutableLiveData<Result>()


    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            flow {

                val response = withContext(Dispatchers.IO) {
                    ApiClient.githubService.getUserGithub()
                }
                emit(response)
            }.onStart {

                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Loading(true)
                }
            }.onCompletion {

                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Loading(false)
                }
            }.catch { e ->

                Log.e("Error", e.message.toString())
                resultUser.value = Result.Error(e)
            }.collect { response ->
                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Success(response)
                }
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            flow {

                val response = withContext(Dispatchers.IO) {
                    ApiClient.githubService.searchUserGithub(
                        mapOf(
                            "q" to username, "per_page" to 15
                        )
                    )
                }
                emit(response)
            }.onStart {

                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Loading(true)
                }
            }.onCompletion {

                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Loading(false)
                }
            }.catch { e ->

                Log.e("Error", e.message.toString())
                resultUser.value = Result.Error(e)
            }.collect { response ->
                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Success(response.items)
                }
            }
        }
    }
}