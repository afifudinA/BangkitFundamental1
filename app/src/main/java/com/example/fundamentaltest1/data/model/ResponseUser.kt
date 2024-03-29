package com.example.fundamentaltest1.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseUser(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<items>
)

@Parcelize
@Entity(tableName = "user")
data class items(
	@ColumnInfo("login")
	@field:SerializedName("login")
	val login: String,

	@ColumnInfo("avatar_url")
	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@PrimaryKey
	@ColumnInfo("id")
	@field:SerializedName("id")
	val id: Int,
) :Parcelable
