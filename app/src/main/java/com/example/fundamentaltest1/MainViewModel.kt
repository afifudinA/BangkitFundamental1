package com.example.fundamentaltest1

import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundamentaltest1.data.model.ResponseUser
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.data.remote.ApiClient
import com.example.fundamentaltest1.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {

    val resultUser = MutableLiveData<Result>()


    fun getUser(){
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                // Make the API call on the IO thread
                val response = withContext(Dispatchers.IO) {
                    ApiClient.githubService.getUserGithub()
                }
                emit(response)
            }.onStart {
                // This block runs when the flow starts
                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Loading(true)
                }
            }.onCompletion {
                // This block runs when the flow completes (successfully or with an exception)
                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Loading(false)
                }
            }.catch { e ->
                // This block runs if there's an exception during the flow
                Log.e("Error", e.message.toString())
                resultUser.value = Result.Error(e)
            }.collect { response ->
                withContext(Dispatchers.Main) {
                    resultUser.value = Result.Success(response)
                }
            }
        }
    }
}