package com.example.fundamentaltest1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fundamentaltest1.data.model.items

@Database(entities = [items::class], version = 1, exportSchema = false)
abstract class AppDb:RoomDatabase() {
    abstract fun userDao():UserDao

}