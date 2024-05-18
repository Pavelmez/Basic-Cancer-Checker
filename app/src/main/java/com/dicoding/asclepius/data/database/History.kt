package com.dicoding.asclepius.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result_history")
data class History(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imageUri: String,
    val result: String
)
