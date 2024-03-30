package com.example.fundamentaltest1.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundamentaltest1.data.local.DbModule
import com.example.fundamentaltest1.detail.DetailViewModel

class FavoriteViewModel(private val dbModule: DbModule):ViewModel() {

    fun getUserFavorite() = dbModule.userDao.loadAll()

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteViewModel(db) as T
    }
}