package com.example.fundamentaltest1.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fundamentaltest1.data.model.items

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: items)

    @Query("SELECT * FROM user")
    fun loadAll(): LiveData<MutableList<items>>

    @Query("SELECT * FROM user WHERE id LIKE :id Limit 1")
    fun findById(id: Int): items

    @Delete
    fun delete(user: items)
}