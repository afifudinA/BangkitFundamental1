package com.example.fundamentaltest1.data.local

import android.content.Context
import androidx.room.Room

class DbModule(private val context: Context) {

    private val db = Room.databaseBuilder(context, AppDb::class.java, "user.db")
        .allowMainThreadQueries().build()

    val userDao = db.userDao()

}