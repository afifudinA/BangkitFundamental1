package com.example.fundamentaltest1.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fundamentaltest1.data.local.DbModule
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.data.remote.ApiClient
import com.example.fundamentaltest1.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val db: DbModule) : ViewModel() {
    val resultDetailUser = MutableLiveData<Result>()
    val resultFollowersUser = MutableLiveData<Result>()
    val resultFollowingUser = MutableLiveData<Result>()
    val resultSuccessFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavorite = false
    fun setFavorite(item: items?) {

        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true
                } else {
                    resultSuccessFavorite.value = true
                    db.userDao.insertUser(item)
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(id: Int, listenerFavorite:() -> Unit){
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null){
                listenerFavorite()
                isFavorite = true
            }
        }
    }

    fun getDetailUser(username: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                // Make the API call on the IO thread
                val response = withContext(Dispatchers.IO) {
                    ApiClient.githubService.getDetailUserGithub(username)
                }
                emit(response)
            }.onStart {
                // This block runs when the flow starts
                withContext(Dispatchers.Main) {
                    resultDetailUser.value = Result.Loading(true)
                }
            }.onCompletion {
                // This block runs when the flow completes (successfully or with an exception)
                withContext(Dispatchers.Main) {
                    resultDetailUser.value = Result.Loading(false)
                }
            }.catch { e ->
                // This block runs if there's an exception during the flow
                Log.e("Error", e.message.toString())
                resultDetailUser.value = Result.Error(e)
            }.collect { response ->
                withContext(Dispatchers.Main) {
                    resultDetailUser.value = Result.Success(response)
                }
            }
        }
    }

    fun getFollowers(username: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                // Make the API call on the IO thread
                val response = withContext(Dispatchers.IO) {
                    ApiClient.githubService.getFollowersGithub(username)
                }
                emit(response)
            }.onStart {
                // This block runs when the flow starts
                withContext(Dispatchers.Main) {
                    resultFollowersUser.value = Result.Loading(true)
                }
            }.onCompletion {
                // This block runs when the flow completes (successfully or with an exception)
                withContext(Dispatchers.Main) {
                    resultFollowersUser.value = Result.Loading(false)
                }
            }.catch { e ->
                // This block runs if there's an exception during the flow
                Log.e("Error", e.message.toString())
                resultFollowersUser.value = Result.Error(e)
            }.collect { response ->
                withContext(Dispatchers.Main) {
                    resultFollowersUser.value = Result.Success(response)
                }
            }
        }
    }

    fun getFollowings(username: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                // Make the API call on the IO thread
                val response = withContext(Dispatchers.IO) {
                    ApiClient.githubService.getFollowingsGithub(username)
                }
                emit(response)
            }.onStart {
                // This block runs when the flow starts
                withContext(Dispatchers.Main) {
                    resultFollowingUser.value = Result.Loading(true)
                }
            }.onCompletion {
                // This block runs when the flow completes (successfully or with an exception)
                withContext(Dispatchers.Main) {
                    resultFollowingUser.value = Result.Loading(false)
                }
            }.catch { e ->
                // This block runs if there's an exception during the flow
                Log.e("Error", e.message.toString())
                resultFollowingUser.value = Result.Error(e)
            }.collect { response ->
                withContext(Dispatchers.Main) {
                    resultFollowingUser.value = Result.Success(response)
                }
            }
        }
    }

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}